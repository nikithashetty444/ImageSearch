package com.arindom.imagesearchapp.network.modals

data class SearchResult(
    val photos: Photos,
    val stat: String
) {
    data class Photos(
        val page: Int,
        val pages: Int,
        val perpage: Int,
        val total: Int,
        val photo: List<Photo>
    ) {
        data class Photo(
            val id: String,
            val owner: String,
            val secret: String,
            val server: String,
            val farm: Int,
            val title: String
        )
    }
}
