package ru.nc.demo.parser.item;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.nc.demo.parser.base.client.exception.ClientParseException;
import ru.nc.demo.parser.base.client.exception.PageLoadException;
import ru.nc.demo.parser.base.manager.AbstractSiteManager;
import ru.nc.demo.parser.base.manager.PageParseException;
import ru.nc.demo.parser.info.AvitoAdvFullInfo;
import ru.nc.demo.parser.info.AvitoAdvShortInfo;
import ru.nc.demo.parser.info.AvitoImageInfo;
import ru.nc.demo.parser.info.MapPoint;
import ru.nc.demo.parser.recognizer.PhoneImageRecognizeException;
import ru.nc.demo.parser.recognizer.PhoneImageRecognizer;
import ru.nc.demo.parser.utils.AvitoDateUtils;
import ru.nc.demo.parser.utils.ThreadUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class AvitoItemParserManager extends AbstractSiteManager {
    private final TypeReference<List<Map<String, Object>>> dataLayerMapperTypeReference = new TypeReference<List<Map<String, Object>>>() {};
    private final Pattern companyUrlPattern = Pattern.compile(".*shopId=([1-9]*).*");
    private final Pattern userUrlPattern = Pattern.compile(".*/user/(.*)/profile.*");
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final AvitoParserClient parserClient;
    private final PhoneImageRecognizer imageRecognizer;
    private final ObjectMapper jsonObjectMapper;

    public AvitoItemParserManager(AvitoParserClient parserClient,
                                  PhoneImageRecognizer imageRecognizer,
                                  ObjectMapper jsonObjectMapper) {
        this.parserClient = parserClient;
        this.imageRecognizer = imageRecognizer;
        this.jsonObjectMapper = jsonObjectMapper;
    }

    /**
     * Метод для получения информации об объявлении
     *
     * @param advShortInfo краткая информация об объявлении
     * @param parseImages  производить ли парсинга изображений объявления
     * @return
     * @throws ClientParseException
     * @throws PageParseException
     */
    public AvitoAdvFullInfo parseFullAdvInfo(AvitoAdvShortInfo advShortInfo, boolean parseImages) throws ClientParseException, PageParseException {
        Document parsedPage = this.loadAdvPage(advShortInfo.getUrl());
        if (parsedPage == null) {
            return null;
        }

        // В этом случае 'Объявление снято с публикации'
        Elements closedWarning = parsedPage.getElementsByClass("item-closed-warning");
        if (closedWarning.size() == 1) {
            return AvitoAdvFullInfo.closed();
        }

        // Без звонков Пользователь предпочитает сообщения
        if (parsedPage.getElementsByClass("item-phone-number").isEmpty()
                && parsedPage.getElementsByAttributeValue("data-marker", "contacts-button/button-card").isEmpty()) {
            logger.warn("Phone element is null, skipping adv");
            return null;
        }

        List<Map<String, Object>> avitoDataLayer = this.parseDataLayer(parsedPage.html());
        Map<String, Object> advDetailMap = avitoDataLayer.get(1);

        Long advId = getLongValue(advDetailMap, "itemID");
        String href = parserClient.getLastRedirectedUrl(advShortInfo.getUrl());

        AvitoAdvFullInfo advFullInfo = new AvitoAdvFullInfo();
        advFullInfo.setId(advId);
        advFullInfo.setHref(href);
        advFullInfo.setShortAddress(advShortInfo.getShortAddress());

        BufferedImage phoneImage = parserClient.loadPhoneImage(advShortInfo.getUrl(), advId);
        try {
            advFullInfo.setPhone(imageRecognizer.analyzePhoneNumber(phoneImage));
        } catch (PhoneImageRecognizeException e) {
            throw new PageLoadException("Error, while recognizing phone number", e);
        }

        Elements mapWrapperElements = parsedPage.getElementsByClass("js-item-map-wrapper");
        if (mapWrapperElements.size() != 1) {
            // Если карта отсутствует, пропускаем сообщение
            return null;
        }

        // Дата объявления
        Date date = advShortInfo.getAdvTimestamp();
        if (date == null) {
            Elements dateElements = parsedPage.getElementsByClass("title-info-metadata-item-redesign");
            if (dateElements.size() != 1) {
                throw new PageParseException(parsedPage.baseUri(), "Elements[class = 'title-info-metadata-item-redesign'].size() != 1");
            }

            try {
                date = AvitoDateUtils.getInstance().convertStringRepresentationToMs(dateElements.get(0).text());
            } catch (ParseException e) {
                throw new PageParseException(parsedPage.baseUri(), "Cannot parse date", e);
            }
        }
        advFullInfo.setDate(date);

        // Координаты
        Element mapWrapperElement = mapWrapperElements.get(0);
        advFullInfo.setGeoPoint(MapPoint.of(
                Double.parseDouble(mapWrapperElement.attr("data-map-lat")),
                Double.parseDouble(mapWrapperElement.attr("data-map-lon"))
        ));

        // Изображения
        if (parseImages) {
            advFullInfo.setImageInfoList(this.loadAdvImages(parsedPage, advShortInfo.getUrl()));
        }

        // Адрес
        Elements sellerPropElements = parsedPage.getElementsByAttributeValue("itemprop", "address");
        String advAddress = sellerPropElements.size() == 0 ? null : sellerPropElements.get(0).text();
        advFullInfo.setFullAddress(advAddress);

        // Описание
        Elements descriptionElements = parsedPage.getElementsByAttributeValue("itemprop", "description");
        String body = descriptionElements.size() == 0 ? null : descriptionElements.get(0).text();
        advFullInfo.setBody(body);

        // Создатель объявления
        Element sellerInfoNameElement = super.getFirstElementByClassValue(parsedPage.baseUri(), parsedPage, "seller-info-name");
        Element creatorTypeElement = super.getFirstElementByAttributeValue(parsedPage.baseUri(), parsedPage, "data-marker", "seller-info/label");
        String sellerUrl = sellerInfoNameElement.childrenSize() == 1 ? sellerInfoNameElement.child(0).attr("href") : null;
        String sellerName = sellerInfoNameElement.text();
        String creatorTypeName = creatorTypeElement.text();

        if (sellerUrl != null) {
            // Попытка разбора, как ссылки юзера
            Matcher userUrlMatcher = userUrlPattern.matcher(sellerUrl);
            if (userUrlMatcher.matches()) {
                advFullInfo.setCreatorUserKey(userUrlMatcher.group(1));
            }
            // Попытка разбора, как ссылки компании
            Matcher companyUrlMatcher = companyUrlPattern.matcher(sellerUrl);
            if (companyUrlMatcher.matches()) {
                advFullInfo.setCreatorShopId(Long.valueOf(companyUrlMatcher.group(1)));
            }
        }

        advFullInfo.setCreatorName(sellerName);
        advFullInfo.setCreatorUrl(sellerUrl);
        advFullInfo.setCreatorTypeName(creatorTypeName);

        // Остальные параметры
        advFullInfo.setKindType(getStringValue(advDetailMap, "type"));
        advFullInfo.setRoomType(getStringValue(advDetailMap, "rooms"));
        advFullInfo.setRightType(getStringValue(advDetailMap, "commission"));
        advFullInfo.setHouseType(advDetailMap.get("house_type").toString());
        advFullInfo.setFloor(getShortValue(advDetailMap, "floor"));
        advFullInfo.setFloorCount(getShortValue(advDetailMap, "floors_count"));
        advFullInfo.setTotalArea(parseArea(advDetailMap, "area"));
        advFullInfo.setKitchenArea(parseArea(advDetailMap, "area_kitchen"));
        advFullInfo.setResidentialArea(parseArea(advDetailMap, "area_live"));
        advFullInfo.setPrice(getLongValue(advDetailMap, "itemPrice"));

        return advFullInfo;
    }

    /**
     * Метод для загрузки страницы объявления
     *
     * @param url url страницы объявления
     * @return полученная страница
     * @throws PageLoadException  в случае ошибки загрузки страницы
     * @throws PageParseException в случае ошибки парсинга страницы
     */
    private Document loadAdvPage(String url) throws PageLoadException, PageParseException {
        int errorCount = 0;
        Document parsedPage = null;
        do {
            try {
                parsedPage = parserClient.loadAdvPage(url);
            } catch (PageLoadException plex) {
                Integer exCode = plex.getStatusCode();
                // 404 или 301 код может вернуться, когда объявление удалено
                if (exCode == HttpStatus.SC_NOT_FOUND
                        || exCode == HttpStatus.SC_MOVED_PERMANENTLY
                        || exCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    logger.warn("Load adv page with " + exCode + " code");
                    return null;
                } else {
                    logger.error("Error while loadAdvPage", plex);
                    throw plex;
                }
            } catch (ClientParseException cex) {
                logger.error("Error while loadAdvPage", cex);
            }

            if (parsedPage != null && parsedPage.childrenSize() == 1) {
                if (parsedPage.child(0).childrenSize() == 2) {
                    if (parsedPage.child(0).child(0).childrenSize() == 0
                            && parsedPage.child(0).child(1).childrenSize() == 0) {
                        parsedPage = null;
                    }
                }
            }

            if (parsedPage == null) {
                errorCount++;
            }
        }
        while (parsedPage == null && errorCount <= AvitoParserSettings.PAGE_ATTEMPTS_LIMIT);

        if (parsedPage == null) {
            throw new PageParseException(url, "Количество попыток загрузить страницу превысило " + AvitoParserSettings.PAGE_ATTEMPTS_LIMIT);
        }

        if (CollectionUtils.isNotEmpty(parsedPage.getElementsByClass("firewall-container"))) {
            ThreadUtils.sleep(AvitoParserSettings.CAPTCHA_CATCHED_DELAY);
            throw new PageParseException(parsedPage.baseUri(), "Ип адрес заблокирован Avito, нужен ввод капчи");
        }

        return parsedPage;
    }

    /**
     * Метод для парсинга window.dataLayer в Map свойств
     *
     * @param pageHtml html страницы
     * @return данные из window.dataLayer в виде Map
     * @throws ClientParseException в случае ошибки парсинга json
     */
    private List<Map<String, Object>> parseDataLayer(String pageHtml) throws ClientParseException {
        int dataStartIndex = pageHtml.indexOf("window.dataLayer");
        while (pageHtml.charAt(dataStartIndex++) != '[');
        int dataEndIndex = dataStartIndex;
        while (pageHtml.charAt(dataEndIndex++) != ']');

        String dataLayerJson = pageHtml.substring(dataStartIndex - 1, dataEndIndex);
        try {
            return jsonObjectMapper.readValue(dataLayerJson, dataLayerMapperTypeReference);
        } catch (IOException e) {
            throw new ClientParseException("Error, while reading dataLayerJson", e);
        }
    }

    private List<AvitoImageInfo> loadAdvImages(Document parsedPage, String baseUrl) {
        Elements smallImageElements = parsedPage.getElementsByClass("gallery-img-frame");
        Elements bigImageElements = parsedPage.getElementsByClass("gallery-extended-img-frame");

        List<AvitoImageInfo> imgInfoList = new ArrayList<>();
        if (CollectionUtils.size(smallImageElements) != 0
                && CollectionUtils.size(smallImageElements) == CollectionUtils.size(bigImageElements)) {

            for (int i = 0; i < smallImageElements.size(); i++) {
                BufferedImage smallImg = loadImage(smallImageElements.get(i), baseUrl);
                BufferedImage bigImg = loadImage(bigImageElements.get(i), baseUrl);

                imgInfoList.add(new AvitoImageInfo(
                        smallImg,
                        bigImg
                ));
            }
        }
        return imgInfoList;
    }

    private BufferedImage loadImage(Element imgElement, String baseUrl) {
        String imgUrl = imgElement.attr("data-url");
        if (StringUtils.isNotEmpty(imgUrl)) {
            if (!imgUrl.startsWith("http")) {
                imgUrl = "https:" + imgUrl;
            }
            try {
                return parserClient.loadImage(baseUrl, imgUrl);
            } catch (ClientParseException e) {
                logger.warn("Error while loading image, url: " + imgUrl, e);
            }
        }
        return null;
    }

    private String getStringValue(Map<String, Object> advDetailMap, String key) {
        Object value = advDetailMap.get(key);
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    private Boolean getBooleanValue(Map<String, Object> advDetailMap, String key) {
        Short shortValue = getShortValue(advDetailMap, key);
        if (shortValue == null) {
            return null;
        }

        return shortValue == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    private Short getShortValue(Map<String, Object> advDetailMap, String key) {
        Object value = advDetailMap.get(key);
        if (value == null) {
            return null;
        }

        return Short.valueOf(value.toString());
    }

    private Long getLongValue(Map<String, Object> advDetailMap, String key) {
        Object value = advDetailMap.get(key);
        if (value == null) {
            return null;
        }

        return Long.valueOf(value.toString());
    }

    private Short parseArea(Map<String, Object> advDetailMap, String key) {
        Object value = advDetailMap.get(key);
        if (value == null) {
            return null;
        }

        String cleanString = value.toString().replace("м²", "").trim();
        BigDecimal areaValue = BigDecimal.valueOf(Double.parseDouble(cleanString));
        areaValue = areaValue.setScale(0, RoundingMode.HALF_UP);
        return areaValue.toBigInteger().shortValue();
    }

}
