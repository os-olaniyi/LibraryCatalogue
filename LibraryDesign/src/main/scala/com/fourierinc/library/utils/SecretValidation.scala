package com.fourierinc.library.utils

object SecretValidation {
  def validate(password: String): Boolean ={
    val minLength = 12
    val hasUpperCase = password.exists(_.isUpper)
    val hasLowerCase = password.exists(_.isLower)
    val hasDigit = password.exists(_.isDigit)
    val hasSpecialChar = password.exists(ch => "!-_*`~".contains(ch))
    
    password.length >= minLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
  }
}