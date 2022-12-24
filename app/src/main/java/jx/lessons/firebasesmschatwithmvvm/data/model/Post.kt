package jx.lessons.firebasesmschatwithmvvm.data.model

class Post {
    var likeS: ArrayList<Likes>? = null
    var comments: ArrayList<Comments>? = null
    var postDescription: String = ""
    var postTitle: String = ""
    var userEmail: String = ""
    var imageUri: String = ""
    var tagsList: ArrayList<String>? = null
    var unixTime:Long=0
    var downloads: ArrayList<Downloads>? = null

    constructor(
        comments: ArrayList<Comments>,
        likes: ArrayList<Likes>,
        postDescription: String,
        postTitle: String,
        userEmail: String,
        imageUri: String,
        tagsList: ArrayList<String>,
        unixTime:Long,
        downloads: ArrayList<Downloads>
    ) {
        this.comments = comments
        this.likeS = likes
        this.postDescription = postDescription
        this.postTitle = postTitle
        this.userEmail = userEmail
        this.imageUri = imageUri
        this.tagsList = tagsList
        this.unixTime = unixTime
        this.downloads = downloads
    }

    constructor()

    constructor(
        comments: ArrayList<Comments>,
        likeS: ArrayList<Likes>,
        postDescription: String,
        postTitle: String,
        userEmail: String,
        tagsList: ArrayList<String>,
        unixTime: Long,
        downloads: ArrayList<Downloads>
    ) {
        this.comments = comments
        this.likeS = likeS
        this.postDescription = postDescription
        this.postTitle = postTitle
        this.userEmail = userEmail
        this.tagsList = tagsList
        this.unixTime = unixTime
        this.downloads=downloads
    }
}