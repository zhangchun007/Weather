package com.maiya.thirdlibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/10/24 16:46
 */
abstract class AacFragment<VM : BaseViewModel, VB : ViewBinding>(layout: Int) :
    BaseFragment(layout) {

    private  var _binding: VB?=null
    protected val binding get() = _binding!!
    protected abstract val vm: VM

    protected abstract fun injectBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): VB


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = injectBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    /**
     * 初始化观察者
     */
    protected open fun initObserve() {
    }

    protected open fun clearBinding(){
        _binding=null
    }




}