package pe.edu.upc.parkingnow.domain.repository

import pe.edu.upc.parkingnow.domain.model.Local

interface LocalRepository {
    suspend fun getLocales(): List<Local>
}

