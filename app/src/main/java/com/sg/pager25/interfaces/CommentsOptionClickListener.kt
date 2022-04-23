package com.sg.pager25.interfaces


import com.sg.pager25.models.Comment


interface CommentsOptionClickListener {
    fun optionMenuClicked(comment: Comment)
}