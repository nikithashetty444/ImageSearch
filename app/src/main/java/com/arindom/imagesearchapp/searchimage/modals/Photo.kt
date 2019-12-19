package com.arindom.imagesearchapp.searchimage.modals

data class PhotoPerPage(var pageNumber: Int, var totalPages: Int, var photoList: List<Photo>)
data class Photo(
    var id: String,
    var name: String,
    var url: String
)