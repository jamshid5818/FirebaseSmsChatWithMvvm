package jx.lessons.firebaseSmsChatWithMvvm.data.model

class UserInfo {
    var age: Int = 0
    var email: String = ""
    var gender: String = ""
    var name: String = ""
    var password: String = ""
    var unixTime:Long=0
    constructor()
    constructor(age: Int, email: String, gender: String, name: String, password: String,unixTime:Long) {
        this.age = age
        this.email = email
        this.gender = gender
        this.name = name
        this.password = password
        this.unixTime = unixTime
    }
}
