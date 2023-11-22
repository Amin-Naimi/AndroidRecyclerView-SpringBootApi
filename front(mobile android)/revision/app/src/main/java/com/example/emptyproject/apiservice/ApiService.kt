package com.example.emptyproject.apiservice

import com.example.emptyproject.Offre
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/offre")
    suspend fun getOffres(): Response<MutableList<Offre>>

    @GET("/offre/{id}")
    suspend fun getOffreById(
        @Path("id") uid: Long

    ): Response<Offre>

    @DELETE("/offre/{id}")
    suspend fun deleteOffre(
        @Path("id") uid: Long
    ): Boolean

    @PUT("/offre/{id}")
    suspend fun updateOffre(
        @Path("id") id: Long,
        @Body updatedOffre: Offre
    ): Response<Offre>

    @POST("/offre")
    suspend fun posteOffre(
        @Body offre: Offre
    ): Response<Offre>
}