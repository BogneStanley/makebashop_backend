package cm.bognestanley.shop_backend.domain.product.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;

public class Product {
        private Long id;
        private String name;
        private String description;
        private List<ProductVariant> productVariants;
        private List<ProductImage> images;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    public Product(Long id, String name, String description, List<ProductVariant> productVariants, List<ProductImage> images,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("Product description cannot be null");
        }
        if (productVariants == null) {
            throw new IllegalArgumentException("Product variants list cannot be null");
        }
        if (images == null) {
            throw new IllegalArgumentException("Product images list cannot be null");
        }

        if (!productVariants.isEmpty()) {
            var firstCurrency = productVariants.get(0).getPrice().currency();
            for (var v : productVariants) {
                if (!v.getPrice().currency().equals(firstCurrency)) {
                    throw new IllegalArgumentException("All product variants must have the same currency");
                }
            }
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.productVariants = productVariants;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Product(String name, String description) {
        this(null, name, description, new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
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

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(String newName, String newDescription, List<ProductVariant> newProductVariants,
            List<ProductImage> newImages) {
        if(newName != null && !newName.isBlank()) {
            this.name = newName;
        }
        if(newDescription != null) {
            this.description = newDescription;
        }
        if(newProductVariants != null) {
            this.productVariants.clear();
            addVariants(newProductVariants);
        }
        if(newImages != null) {
            this.images.clear();
            addImages(newImages);
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void addVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("Product variant cannot be null");
        }

        boolean skuExists = productVariants.stream()
                .anyMatch(v -> v.getSku().equalsIgnoreCase(variant.getSku()));
        if (skuExists) {
            throw new IllegalArgumentException("Product variant with SKU " + variant.getSku() + " already exists");
        }

        if (!productVariants.isEmpty()) {
            var productCurrency = productVariants.get(0).getPrice().currency();
            if (!variant.getPrice().currency().equals(productCurrency)) {
                throw new IllegalArgumentException("Variant currency " + variant.getPrice().currency() +
                        " does not match product currency " + productCurrency);
            }
        }

        this.productVariants.add(variant);
        this.updatedAt = LocalDateTime.now();
    }

    public void addVariants(List<ProductVariant> variants) {
        if (variants == null) {
            throw new IllegalArgumentException("Variants list cannot be null");
        }
        for (ProductVariant variant : variants) {
            addVariant(variant);
        }
    }


    public void removeVariantBySKU(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }

        boolean removed = productVariants.removeIf(v -> v.getSku().equalsIgnoreCase(sku));

        if (!removed) {
            throw new IllegalArgumentException("Product variant with SKU " + sku + " not found");
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void removeVariants(List<Long> variantIds) {
        // Store original size for validation
        int originalSize = productVariants.size();
        
        // Remove variants by ID
        productVariants.removeIf(v -> variantIds.contains(v.getId()));
        
        // Check if any variants were removed
        if (productVariants.size() == originalSize) {
            throw new IllegalArgumentException("No variants found with the given IDs");
        }
        
        // Check if there's still at least one variant
        if (productVariants.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete all variants. Product must have at least one variant.");
        }
        
        this.updatedAt = LocalDateTime.now();
    }

    public void addImage(ProductImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Product image cannot be null");
        }
        if (images.isEmpty()) {
            image.markAsPrimary();
        }
        image.updatePosition(images.size() + 1);
        this.images.add(image);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateImagePosition(Long imageId, int newPosition) {
        if (imageId == null) {
            throw new IllegalArgumentException("Image ID cannot be null");
        }
        if (newPosition < 1 || newPosition > images.size()) {
            throw new IllegalArgumentException("Invalid position. Position must be between 1 and " + images.size());
        }
        if (images.size() == 1) {
            return; // nothing to reorder
        }
        
        // Find the image
        Optional<ProductImage> imageOpt = images.stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst();
        
        if (!imageOpt.isPresent()) {
            throw new IllegalArgumentException("Product image with ID " + imageId + " not found");
        }
        
        ProductImage imageToMove = imageOpt.get();
        
        // Remove from current position
        List<ProductImage> updatedImages = new ArrayList<>(images);
        updatedImages.remove(imageToMove);
        
        // Insert at new position (convert to 0-based index)
        updatedImages.add(newPosition - 1, imageToMove);
        
        // Reassign positions
        for (int i = 0; i < updatedImages.size(); i++) {
            ProductImage img = updatedImages.get(i);
            img.updatePosition(i + 1);
        }
        
        // Update images list
        this.images = updatedImages;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateImagesPosition(Map<Long, Integer> commands) {
        if (commands == null) {
            throw new IllegalArgumentException("Commands list cannot be null");
        }
        if (commands.isEmpty()) {
            return; // nothing to reorder
        }
        
        // Find the images to move
        List<ProductImage> imagesToMove = new ArrayList<>();
        
        for (Map.Entry<Long, Integer> entry : commands.entrySet()) {
            Long imageId = entry.getKey();
            Optional<ProductImage> imageOpt = images.stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst();
            
            if (!imageOpt.isPresent()) {
                throw new IllegalArgumentException("Product image with ID " + imageId + " not found");
            }
            
            imagesToMove.add(imageOpt.get());
        }
        
        // Remove images from current positions
        List<ProductImage> updatedImages = new ArrayList<>(images);
        updatedImages.removeAll(imagesToMove);
        
        // Insert at new positions
        for (Map.Entry<Long, Integer> entry : commands.entrySet()) {
            Long imageId = entry.getKey();
            Integer newPosition = entry.getValue();
            ProductImage image = imagesToMove.stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst()
                    .get();
            updatedImages.add(newPosition - 1, image);
        }
        
        // Reassign positions
        for (int i = 0; i < updatedImages.size(); i++) {
            ProductImage img = updatedImages.get(i);
            img.updatePosition(i + 1);
        }
        
        // Update images list
        this.images = updatedImages;
        this.updatedAt = LocalDateTime.now();
    }

    public void removeImage(Long imageId) {
        if (imageId == null) {
            throw new IllegalArgumentException("Image ID cannot be null");
        }
        Optional<ProductImage> imageOpt = images.stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst();
        
        if (!imageOpt.isPresent()) {
            throw new IllegalArgumentException("Product image with ID " + imageId + " not found");
        }
        
        ProductImage imageToRemove = imageOpt.get();
        
        // Remove from list
        this.images.remove(imageToRemove);
        
        // Update positions of remaining images
        updatePositionsOfRemainingImages();
        
        // If the removed image was primary, select the new primary
        if (imageToRemove.isPrimary() && !images.isEmpty()) {
            images.get(0).markAsPrimary();
        }
        
        this.updatedAt = LocalDateTime.now();
    }

    public void updateImages(List<ProductImage> newImages) {
        if (newImages == null) {
            throw new IllegalArgumentException("Product images list cannot be null");
        }
        this.images = newImages;
        this.updatedAt = LocalDateTime.now();
    }

    public Optional<Money> getPriceRangeMin() {
        return productVariants.stream()
                .map(ProductVariant::getPrice)
                .min((m1, m2) -> m1.amount().compareTo(m2.amount()));
    }

    public Optional<Money> getPriceRangeMax() {
        return productVariants.stream()
                .map(ProductVariant::getPrice)
                .max((m1, m2) -> m1.amount().compareTo(m2.amount()));
    }

    public int getTotalStock() {
        return productVariants.stream()
                .mapToInt(ProductVariant::getStockQuantity)
                .sum();
    }

    public boolean isAnyVariantAvailable() {
        return productVariants.stream()
                .anyMatch(ProductVariant::isAvailable);
    }

    public void addImages(List<ProductImage> productImages) {
        if(productImages == null) {
            throw new IllegalArgumentException("Product images cannot be null");
        }
        for(ProductImage productImage : productImages) {
            this.addImage(productImage);
        }
    }


    public List<ProductImage> removeImagesByIds(List<Long> imageIds) {
        List<ProductImage> removedImages = new ArrayList<>();
        for(Long imageId : imageIds) {
            Optional<ProductImage> imageOpt = images.stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst();
            
            if (!imageOpt.isPresent()) {
                throw new IllegalArgumentException("Product image with ID " + imageId + " not found");
            }
            removedImages.add(imageOpt.get());
            this.images.remove(imageOpt.get());
        }

        // Update positions of remaining images
        updatePositionsOfRemainingImages();

        // If the removed image was primary, select the new primary
        if(images.stream().noneMatch(ProductImage::isPrimary) && !images.isEmpty()) {
            images.get(0).markAsPrimary();
        }
        

        this.updatedAt = LocalDateTime.now();
        return removedImages;
    }

    private void updatePositionsOfRemainingImages() {
        for (int i = 0; i < images.size(); i++) {
            ProductImage img = images.get(i);
            img.updatePosition(i + 1);
        }
    }


    public void setImageAsPrimary(Long imageId) {

        this.images = this.images.stream().map(img -> {
            if (img.getId().equals(imageId)) {
                img.markAsPrimary();
            } else {
                img.markAsNotPrimary();
            }
            return img;
        }).toList();

        this.updatedAt = LocalDateTime.now();
    }


    public void updateProductVariant(ProductVariant domainEntity) {
        this.productVariants = this.productVariants.stream().map(variant -> {
            if(variant.getId().equals(domainEntity.getId())) {
                return variant.update(domainEntity.getSku(), domainEntity.getPrice(), domainEntity.getStockQuantity(), domainEntity.getSize(), domainEntity.getColor());
            }
            return variant;
        }).toList();
        this.updatedAt = LocalDateTime.now();
    }
}
