package com.otpless.reactnative

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class OtplessReactPackage : ReactPackage {
    override fun createNativeModules(reactAppContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(
            OtplessReactModule(reactAppContext)
        )
    }

    override fun createViewManagers(reactAppContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> =
        mutableListOf()
}