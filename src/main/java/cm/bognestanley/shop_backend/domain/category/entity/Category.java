package cm.bognestanley.shop_backend.domain.category.entity;

public class Category {
    private Long id;
    private String name;
    private String description;

    public Category(Long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Category create(String name, String description){
        return new Category(null, name, description);
    }

    public void update(String name, String description){
        if (name != null){
            this.name = name;
        }
        if (description != null){
            this.description = description;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
