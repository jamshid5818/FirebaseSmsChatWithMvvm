package jx.lessons.firebaseSmsChatWithMvvm.data.model

class Comments {
    var email: String = ""
    var commentText: String = ""
    var unixTime:Long=0

    constructor()
    constructor(email: String, commentText: String,unixTime:Long) {
        this.email = email
        this.commentText = commentText
        this.unixTime = unixTime
    }
}