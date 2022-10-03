package jx.lessons.firebasesmschatwithmvvm.data.model

class Comments {
    var email: String = ""
    var commentText: String = ""

    constructor()
    constructor(email: String, commentText: String) {
        this.email = email
        this.commentText = commentText
    }
}