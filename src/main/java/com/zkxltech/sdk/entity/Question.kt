package com.zkxltech.sdk.entity

/**
 * Create by Panda on 2019-05-30
 *
 * type(1byte): 0->单选题  1->多选题  2->判断题  3->数字题
 *       4->通用题  5->小数题  6->投票题  7->评分题
 *
 * id(1byte): 题目序号
 *
 * range(1-3bytes):数字题(1-2bytes)  小数题(3bytes)  其他题(1byte)
 */
data class Question(var type: String, var id: Int, var range: Float) {

    constructor(type: String, id: Int) : this(type, id, 100f)

}