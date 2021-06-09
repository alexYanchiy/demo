package ru.nc.demo.parser.info;

/**
 * Класс, описывающий рубрику на сайте avito.ru
 *
 * @author NiggerCat
 */
public final class Rubric {

    // Имя рубрики
    private String name;

    // Отображаемое имя рубрики
    private String visibleName;

    // Подрубрика данной рубрики
    private Rubric subRubric;

    /**
     * Конструктор класса Rubric
     *
     * @param name        путь рубрики
     * @param visibleName отображаемое имя рубрики
     */
    public Rubric(String name, String visibleName) {
        this(name, visibleName, null);
    }

    /**
     * Конструктор класса Rubric
     *
     * @param name        путь рубрики
     * @param visibleName отображаемое имя рубрики
     * @param subRubric   список подрубрик данной рубрики
     */
    public Rubric(String name, String visibleName, Rubric subRubric) {
        this.name = name;
        this.visibleName = visibleName;
        this.subRubric = subRubric;
    }

    public String getName() {
        return name;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public Rubric getSubRubric() {
        return subRubric;
    }

    public String gatherFullPath() {
        String path = getName();
        if (subRubric != null) {
            path += "/" + subRubric.getName();
        }

        return path;
    }

    @Override
    public String toString() {
        return "Rubric [" + name + "]";
    }

}
