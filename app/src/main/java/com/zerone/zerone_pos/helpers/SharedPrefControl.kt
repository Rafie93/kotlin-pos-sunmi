package com.zerone.zerone_pos.helpers
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import android.content.Context
import com.zerone.zerone_pos.helpers.Const.TOKEN_LOGIN
import com.zerone.zerone_pos.ui.login.UserData

class SharedPrefControl private constructor(private val mCtx: Context) {

    fun isLogin():Boolean?{
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Const.IS_LOGIN,false)
    }

    fun sLogin(nilai: Boolean) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(Const.IS_LOGIN, nilai)
        editor.apply()

    }

    fun getToken(): String? {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Const.TOKEN_LOGIN,"-")
    }

    fun saveToken(token:String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_LOGIN, "Bearer "+token)
        editor.apply()

    }

    fun saveString(key: String,isi:String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, isi)
        editor.apply()

    }

    fun saveInt(key: String,isi:Int) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(key, isi)
        editor.apply()
    }
    fun saveBoolean(key: String,aktif:Boolean) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, aktif)
        editor.apply()

    }
    fun getInt(key: String):Int {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key,0)

    }

    fun getBoolean(key: String):Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key,false)
    }

    fun getString(key:String): String? {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key,"-")
    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    companion object {
        private val SHARED_PREF_NAME = "zerone_pos_shared_preff"

        private var mInstance: SharedPrefControl? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefControl {
            if (mInstance == null) {
                mInstance = SharedPrefControl(mCtx)
            }
            return mInstance as SharedPrefControl
        }

    }

}