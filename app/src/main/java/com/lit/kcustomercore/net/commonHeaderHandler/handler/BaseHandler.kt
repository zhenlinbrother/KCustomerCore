package com.lit.kcustomercore.net.commonHeaderHandler.handler

import okhttp3.Request
import java.net.CacheRequest

/**
 * <责任链处理类> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/16
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
abstract class BaseHandler {

    //下一个处理者
    private var nextHandler: BaseHandler? = null

    /**
     * 获取对应的头签名参数
     */
    private fun getHeaderParams(request: Request, headParams : Map<String, String>){
        if (isHandler(request, headParams)){
            handle(request, headParams)
        }

        nextHandler?.getHeaderParams(request, headParams)
    }

    /**
     * 获取当前处理者是否要进行处理
     */
    protected abstract fun isHandler(request: Request, headParams: Map<String, String>) : Boolean

    /**
     * 当前处理的逻辑
     */
    protected abstract fun handle(request: Request, headParams: Map<String, String>)
}