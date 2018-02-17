package com.kontus.cryptocurrencymanager.helpers

import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class General {
    fun getKeyHashForApp(packageManager: PackageManager) {
        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo("com.kontus.cryptocurrencymanager", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())

                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: Throwable) {
            Log.e("KeyHash:", e.message)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash:", e.message)
        }
    }
}