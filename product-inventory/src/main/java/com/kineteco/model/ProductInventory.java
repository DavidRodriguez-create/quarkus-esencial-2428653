package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ProductInventory extends PanacheEntity {

   @Null(groups = ValidationGroups.Put.class)
   @NotBlank(groups = ValidationGroups.Post.class)
   public String sku;
   public String category;

   @NotBlank(message = "Name is mandatory and should be provided")
   public String name;

   public int quantity;
   public String powerWatts;
   public String footprint;
   public BigDecimal manufacturingCost;
   public BigDecimal price;

   @Enumerated(EnumType.STRING)
   public ProductLine productLine;

   @Convert(converter = ConsumerTypeConverter.class)
   public List<ConsumerType> targetConsumer = new ArrayList<>();

   @Enumerated(EnumType.STRING)
   public ProductAvailability productAvailability;

   @PositiveOrZero
   public int unitsAvailable;

   public static ProductInventory findBySku(String sku) {
      return find("sku", sku).firstResult();
   }

   public static int findCurrentStock(String sku) {
      UnitsAvailable unitsAvailable = find("sku", sku).project(UnitsAvailable.class).firstResult();

      if (unitsAvailable == null) {
         return 0;
      }

      return unitsAvailable.unitsAvailable;
   }

   @Override
   public String toString() {
      return "ProductInventory{" + "sku='" + sku + '\'' + ", category='" + category + '\'' + ", name='" + name + '\''
            + ", quantity=" + quantity + ", powerWatts=" + powerWatts + ", footprint=" + footprint
            + ", manufacturingCost=" + manufacturingCost + ", price=" + price + ", productLine=" + productLine
            + ", targetConsumer=" + targetConsumer.toString() + ", productAvailability=" + productAvailability
            + ", unitsAvailable=" + unitsAvailable + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      ProductInventory that = (ProductInventory) o;
      return quantity == that.quantity && Objects.equals(powerWatts, that.powerWatts) && Objects
            .equals(footprint, that.footprint) && unitsAvailable == that.unitsAvailable && Objects.equals(sku, that.sku)
            && Objects.equals(category, that.category) && Objects.equals(name, that.name) && Objects
            .equals(manufacturingCost, that.manufacturingCost) && Objects.equals(price, that.price)
            && productLine == that.productLine && Objects.equals(targetConsumer, that.targetConsumer)
            && productAvailability == that.productAvailability;
   }

   @Override
   public int hashCode() {
      return Objects
            .hash(sku, category, name, quantity, powerWatts, footprint, manufacturingCost, price, productLine,
                  targetConsumer, productAvailability, unitsAvailable);
   }

   public String getSku() {
      return sku;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getQuantity() {
      return quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public String getPowerWatts() {
      return powerWatts;
   }

   public void setPowerWatts(String powerWatts) {
      this.powerWatts = powerWatts;
   }

   public String getFootprint() {
      return footprint;
   }

   public void setFootprint(String footprint) {
      this.footprint = footprint;
   }

   public BigDecimal getManufacturingCost() {
      return manufacturingCost;
   }

   public void setManufacturingCost(BigDecimal manufacturingCost) {
      this.manufacturingCost = manufacturingCost;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public ProductLine getProductLine() {
      return productLine;
   }

   public void setProductLine(ProductLine productLine) {
      this.productLine = productLine;
   }

   public List<ConsumerType> getTargetConsumer() {
      return targetConsumer;
   }

   public void setTargetConsumer(List<ConsumerType> targetConsumer) {
      this.targetConsumer = targetConsumer;
   }

   public ProductAvailability getProductAvailability() {
      return productAvailability;
   }

   public void setProductAvailability(ProductAvailability productAvailability) {
      this.productAvailability = productAvailability;
   }

   public int getUnitsAvailable() {
      return unitsAvailable;
   }

   public void setUnitsAvailable(int unitsAvailable) {
      this.unitsAvailable = unitsAvailable;
   }
}
