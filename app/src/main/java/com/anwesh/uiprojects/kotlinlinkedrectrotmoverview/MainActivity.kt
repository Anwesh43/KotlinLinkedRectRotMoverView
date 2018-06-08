package com.anwesh.uiprojects.kotlinlinkedrectrotmoverview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkedrectrotmoverview.LinkedRectRotMoverView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedRectRotMoverView.create(this)
    }
}
