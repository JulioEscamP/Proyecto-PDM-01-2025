package com.jejhdmdv.proyecto_pdm.data.remote.admin

import com.jejhdmdv.proyecto_pdm.model.admin.Product
import com.jejhdmdv.proyecto_pdm.model.admin.ProductRequest
import com.jejhdmdv.proyecto_pdm.model.admin.ProductResponse
import retrofit2.Response
import retrofit2.http.*


 //API para la gestión de productos por parte del administrador

interface ProductApi {
    

    @GET("admin/products")
    suspend fun getAllProducts(): Response<ProductResponse>
    

    @GET("admin/products/{id}")
    suspend fun getProductById(@Path("id") productId: String): Response<ProductResponse>
    

    @POST("admin/products")
    suspend fun createProduct(@Body productRequest: ProductRequest): Response<ProductResponse>
    

    @PUT("admin/products/{id}")
    suspend fun updateProduct(
        @Path("id") productId: String,
        @Body productRequest: ProductRequest
    ): Response<ProductResponse>
    

    @DELETE("admin/products/{id}")
    suspend fun deleteProduct(@Path("id") productId: String): Response<ProductResponse>
    

    @GET("products")
    suspend fun getActiveProducts(): Response<ProductResponse>
}

