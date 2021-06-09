package ru.nc.demo.parser.info;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Класс предназначен для хранения населенных пунктов Крыма
 *
 * @author NiggerCat
 */
public final class LocalityStore {

    public static Locality findByName(String name) {
        return LOCALITY_BY_NAME_MAP.get(name);
    }

    public static Locality findByVisibleName(String visibleName) {
        return findByVisibleNameInternal(visibleName, RUSSIA);
    }

    private static Locality findByVisibleNameInternal(String visibleName, Locality locality) {
        if (visibleName.equals(locality.getVisibleName())) {
            return locality;
        }

        for (Locality loc : locality.getChildLocalityList()) {
            return findByVisibleNameInternal(visibleName, loc);
        }

        return null;
    }

    // Список населенным пунктов Крыма, включая сам Крым
    private static final List<Locality> KRYM_LOCALITY_LIST = Arrays.asList(
            new Locality("alupka", "Алупка"),
            new Locality("alushta", "Алушта"),
            new Locality("armyansk", "Армянск"),
            new Locality("bahchisaray", "Бахчисарай"),
            new Locality("belogorsk", "Белогорск"),
            new Locality("gaspra", "Гаспра"),
            new Locality("gvardeyskoe", "Гвардейское"),
            new Locality("gresovskiy", "Грэсовский"),
            new Locality("dzhankoy", "Джанкой"),
            new Locality("evpatoriya", "Евпатория"),
            new Locality("zhavoronki", "Жаворонки"),
            new Locality("inkerman", "Инкерман"),
            new Locality("kahovskoe", "Каховское"),
            new Locality("kerch", "Керчь"),
            new Locality("krasnogvardeyskoe", "Красногвардейское"),
            new Locality("krasnoperekopsk", "Красноперекопск"),
            new Locality("kubanskoe", "Кубанское"),
            new Locality("morskaya", "Морская"),
            new Locality("ukraina_novyy_svet", "Новый Свет"),
            new Locality("oktyabrskoe", "Октябрьское"),
            new Locality("oreanda", "Ореанда"),
            new Locality("primorskiy", "Приморский"),
            new Locality("saki", "Саки"),
            new Locality("sevastopol", "Севастополь"),
            new Locality("severnaya", "Северная"),
            new Locality("simonenko", "Симоненко"),
            new Locality("simferopol", "Симферополь"),
            new Locality("staryy_krym", "Старый Крым"),
            new Locality("sudak", "Судак"),
            new Locality("avtonomnaya_respublika_krym_ukrainka", "Украинка"),
            new Locality("feodosiya", "Феодосия"),
            new Locality("fersmanovo", "Ферсманово"),
            new Locality("chernomorskoe", "Черноморское"),
            new Locality("chistenkaya", "Чистенькая"),
            new Locality("schelkino", "Щёлкино"),
            new Locality("yalta", "Ялта")
    );

    // Область Крым
    public static final Locality KRYM = new Locality("respublika_krym", "Весь Крым", KRYM_LOCALITY_LIST);

    // Россия
    private static final Locality RUSSIA = new Locality("rossiya", "Россия", Collections.singletonList(KRYM));

    private static final HashMap<String, Locality> LOCALITY_BY_NAME_MAP = new HashMap<>();
    static {
        fillLocalityByNameMap(RUSSIA, LOCALITY_BY_NAME_MAP);
    }

    private static void fillLocalityByNameMap(Locality locality, HashMap<String, Locality> resultMap) {
        resultMap.put(locality.getName(), locality);
        if (CollectionUtils.isNotEmpty(locality.getChildLocalityList())) {
            for (Locality childLocality : locality.getChildLocalityList()) {
                fillLocalityByNameMap(childLocality, resultMap);
            }
        }
    }

}

