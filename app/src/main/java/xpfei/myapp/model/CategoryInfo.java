package xpfei.myapp.model;

/**
 * Description: 首页分类的javabeaan
 * Author: xpfei
 * Date:   2017/08/22
 */
public class CategoryInfo {
    private String name;
    private int drawableId;
    private int type;

    public CategoryInfo(String name, int drawableId, int type) {
        this.name = name;
        this.drawableId = drawableId;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
