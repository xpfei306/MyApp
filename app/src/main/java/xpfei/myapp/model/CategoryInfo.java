package xpfei.myapp.model;

/**
 * Description: 首页分类的javabeaan
 * Author: xpfei
 * Date:   2017/08/22
 */
public class CategoryInfo {
    private String name;
    private int drawableId;
    public CategoryInfo() {

    }

    public CategoryInfo(String name, int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
