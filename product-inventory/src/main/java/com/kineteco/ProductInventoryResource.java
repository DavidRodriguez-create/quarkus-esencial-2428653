package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ValidationGroups;
import com.kineteco.repository.ProductInventoryRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

@Path("/products")
public class ProductInventoryResource {
    private static final Logger LOGGER = Logger.getLogger(ProductInventoryResource.class);

    @Inject
    ProductInventoryRepository productInventoryRepository;

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/health")
    public String health() {
        LOGGER.debug("health called");
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ProductInventory> listInventory() {
        LOGGER.debug("Product inventory list");
        return productInventoryRepository.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}")
    public Response inventory(@PathParam("sku") String sku) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = productInventoryRepository.findBySku(sku);

        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(productInventory).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory) {
        LOGGER.debugf("create %s", productInventory);

        productInventoryRepository.persist(productInventory);

        return Response.created(URI.create(productInventory.getSku())).build();
    }

    @PUT
    @Path("/{sku}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateProduct(@PathParam("sku") String sku, @ConvertGroup(to = ValidationGroups.Put.class)  @Valid ProductInventory productInventory) {
        LOGGER.debugf("update %s", productInventory);
        ProductInventory existingProduct = productInventoryRepository.findBySku(sku);
        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingProduct.setName(productInventory.getName());
        existingProduct.setCategory(productInventory.getCategory());
        //...
        productInventoryRepository.persist(existingProduct);
        return Response.accepted(productInventory).build();
    }

    @PATCH
    @Path("/{sku}")
    @Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
    @Transactional
    public Response updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
        LOGGER.debugf("get by sku %s", sku);
        int currentStock = productInventoryRepository.findCurrentStock(sku);

        productInventoryRepository.update("unitsAvailable = ?1 where sku= ?2", currentStock + stock, sku);

        return Response.accepted(URI.create(sku)).build();
    }

    @DELETE
    @Path("/{sku}")
    @Transactional
    public Response delete(@PathParam("sku") String sku) {
        LOGGER.debugf("delete by sku %s", sku);
        productInventoryRepository.delete("sku", sku);
        return Response.accepted().build();
    }

}
