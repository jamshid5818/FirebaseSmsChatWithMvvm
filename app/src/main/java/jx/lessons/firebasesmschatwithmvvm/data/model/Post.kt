package jx.lessons.firebasesmschatwithmvvm.data.model

class Post {
    lateinit var comments: ArrayList<Comments>
    lateinit var likeS: ArrayList<Likes>
    var postDescription: String = ""
    var postTitle: String = ""
    var userEmail: String = ""
    var imageUri: String = ""
    lateinit var tagsList: ArrayList<String>
    var randomKey:String = ""

    constructor(
        comments: ArrayList<Comments>,
        likes: ArrayList<Likes>,
        postDescription: String,
        postTitle: String,
        userEmail: String,
        imageUri: String,
        tagsList: ArrayList<String>,
        randomkey: String
    ) {
        this.comments = comments
        this.likeS = likes
        this.postDescription = postDescription
        this.postTitle = postTitle
        this.userEmail = userEmail
        this.imageUri = imageUri
        this.tagsList = tagsList
        this.randomKey = randomkey
    }

    constructor()

    constructor(
        comments: ArrayList<Comments>,
        likeS: ArrayList<Likes>,
        postDescription: String,
        postTitle: String,
        userEmail: String,
        tagsList: ArrayList<String>,
        randomkey: String
    ) {
        this.comments = comments
        this.likeS = likeS
        this.postDescription = postDescription
        this.postTitle = postTitle
        this.userEmail = userEmail
        this.tagsList = tagsList
        this.randomKey = randomkey
    }
}