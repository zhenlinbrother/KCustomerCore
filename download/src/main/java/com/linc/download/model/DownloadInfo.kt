package com.linc.download.model

import com.linc.download.db.DownloadDB
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel


@Table(database = DownloadDB::class)
class DownloadInfo : BaseModel() {
    // ============================ 持久化信息 ==================================
    @PrimaryKey(autoincrement = true)
    var id : Long = 0

    @Column
    var url : String? = null

    @Column
    var fileName : String? = null

    @Column
    var createTime: Long = 0

    @Column
    var tmpFileName: String? = null

    @Column
    var status: Int = 0
}

