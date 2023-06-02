package com.otpless.reactnative

import android.app.Activity
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.otpless.dto.OtplessResponse
import com.otpless.views.OtplessManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Suppress("unused")
class OtplessReactModule(private val reactAppContext: ReactApplicationContext) : ReactContextBaseJavaModule() {

    override fun getName(): String {
        return "OtplessModule"
    }

    @ReactMethod
    fun startOtplessWithEvent() {
        OtplessManager.getInstance().start { result: OtplessResponse ->
            val jsonObject = JSONObject()
            try {
                jsonObject.put("errorMessage", result.errorMessage)
                jsonObject.put("data", result.data)
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
            sendResultEvent(jsonObject)
        }
    }

    @ReactMethod
    fun startOtplessWithCallback(callback: Callback) {
        OtplessManager.getInstance().showFabButton(false)
        OtplessManager.getInstance().start { result: OtplessResponse ->
            val jsonObject = JSONObject()
            try {
                jsonObject.put("errorMessage", result.errorMessage)
                jsonObject.put("data", result.data)
                val resultMap = convertJsonToMap(jsonObject)
                callback.invoke(resultMap)
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }
    }

    @ReactMethod
    fun onSignInCompleted() {
        val activity: Activity = reactAppContext.currentActivity ?: return
        activity.runOnUiThread { OtplessManager.getInstance().onSignInCompleted() }
    }

    private fun sendResultEvent(result: JSONObject) {
        try {
            val map = convertJsonToMap(result)
            this.reactAppContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("OTPlessSignResult", map)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    @Throws(JSONException::class)
    private fun convertJsonToMap(jsonObject: JSONObject): WritableMap {
        val map: WritableMap = WritableNativeMap()
        val iterator = jsonObject.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            val value = jsonObject[key]
            if (value is JSONObject) {
                map.putMap(key, convertJsonToMap(value))
            } else if (value is JSONArray) {
                map.putArray(key, convertJsonToArray(value))
            } else if (value is Boolean) {
                map.putBoolean(key, value)
            } else if (value is Int) {
                map.putInt(key, value)
            } else if (value is Double) {
                map.putDouble(key, value)
            } else if (value is String) {
                map.putString(key, value)
            } else {
                map.putString(key, value.toString())
            }
        }
        return map
    }

    @Throws(JSONException::class)
    private fun convertJsonToArray(jsonArray: JSONArray): WritableArray {
        val array: WritableArray = WritableNativeArray()
        for (i in 0 until jsonArray.length()) {
            val value = jsonArray[i]
            if (value is JSONObject) {
                array.pushMap(convertJsonToMap(value))
            } else if (value is JSONArray) {
                array.pushArray(convertJsonToArray(value))
            } else if (value is Boolean) {
                array.pushBoolean(value)
            } else if (value is Int) {
                array.pushInt(value)
            } else if (value is Double) {
                array.pushDouble(value)
            } else if (value is String) {
                array.pushString(value)
            } else {
                array.pushString(value.toString())
            }
        }
        return array
    }

}