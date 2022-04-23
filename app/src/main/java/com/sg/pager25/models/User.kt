package com.sg.pager25.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    var dio: String = "",
    val profileCompleted: Int = 0
) : Parcelable

/*data class User(
    var userName: String = "",
    var fullName: String = "",
    var email: String = "",
    var profileImage: String = "",
    var dio: String = "",
    var uid: String = ""
)*/