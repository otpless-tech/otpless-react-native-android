package com.otpless.reactnative

import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.otpless.views.OtplessManager

@Suppress("unused")
class OtplessReactModule(private val reactAppContext: ReactApplicationContext) : ReactContextBaseJavaModule() {

    override fun getName(): String {
        return "OtplessModule"
    }

    @ReactMethod
    fun openOtplessSdk(callback: Callback) {
        OtplessManager.getInstance().start { result -> callback.invoke(result.toString()) }
    }
}