package jx.lessons.firebaseSmsChatWithMvvm.data.model

class Downloads {
    var email: String = ""
    var unixTime:Long=0
    constructor()
    constructor(email: String, unixTime:Long) {
        this.email = email
        this.unixTime=unixTime
    }
}