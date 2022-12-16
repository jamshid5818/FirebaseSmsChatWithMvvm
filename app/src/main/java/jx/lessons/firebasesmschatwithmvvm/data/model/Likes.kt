package jx.lessons.firebasesmschatwithmvvm.data.model

class Likes {
    var email: String = ""
    var unixTime:Long=0
    constructor()
    constructor(email: String, unixTime:Long) {
        this.email = email
        this.unixTime=unixTime
    }
}