package com.maiya.thirdlibrary.utils

import android.app.Activity
import java.util.*
import kotlin.system.exitProcess

object ActivityManageTools {

    private val mActivityStack: Stack<Activity> by lazy {
        Stack<Activity>()
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    fun topActivity(): Activity? {
        if (mActivityStack.size > 0) {
            return mActivityStack.lastElement()
        }
        return null
    }


    /**
     * 获取栈中存储个数
     */
    fun getActivityStackSize(): Int {
        return mActivityStack.size
    }

    fun isActivityAdded(ac: Class<*>): Boolean {
        return mActivityStack.filter { it.javaClass == ac }.size >= 0
    }


    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (null != activity)
            mActivityStack.add(activity)
    }

    /**
     * 退出应用程序
     */
    fun exit() {
        try {
            for (activity in mActivityStack) {
                activity.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            exitProcess(0)
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllWithProcess() {
        try {
            for (activity in mActivityStack) {
                mActivityStack.remove(activity)
                if (!activity.isFinishing&&!activity.isDestroyed){
                    activity.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mActivityStack.clear()
            exitProcess(0)
        }
    }


    fun finishAll() {
        try {
            for (activity in mActivityStack) {
                mActivityStack.remove(activity)
                activity.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mActivityStack.clear()
        }
    }

    /**
     * 移除Activity到堆栈
     */
    fun removeActivity(activity: Activity) {
        if (activity in mActivityStack)
            mActivityStack.remove(activity)
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    fun killTopActivity() {
        val activity = topActivity()
        killActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun killActivity(activity: Activity?) {
        if (activity != null) {
            if (activity in mActivityStack) {
                mActivityStack.remove(activity)
            }
            activity.finish()
        }
    }

    fun removeAboveActivities(ac: Class<*>, func: () -> Unit) {
        try {
            var activity = mActivityStack.peek()
            while (activity.javaClass != ac) {
                activity.finish()
                activity = mActivityStack.pop()
            }
        } catch (e: Exception) {

        } finally {
            func()
        }
    }
}