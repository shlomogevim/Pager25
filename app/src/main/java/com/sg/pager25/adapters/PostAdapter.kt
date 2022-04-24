package com.sg.pager25.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sg.pager20.adapters.DrawPostCenter
import com.sg.pager25.R
import com.sg.pager25.models.Post
import com.sg.pager25.activities_social.PostDetailsActivity
import com.sg.pager25.utilities.Constants.POST_EXSTRA
import com.sg.pager25.utilities.UtilityPost
import kotlin.collections.ArrayList

//class PostAdapter(context:Context, val posts:ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PagerViewHolder>(){
class PostAdapter(val viewPager: ViewPager2, val context: Context, val posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.PagerViewHolder>() {

  val drawPost = DrawPostCenter(context)


    val util = UtilityPost()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PagerViewHolder(view)
    }


    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bindImage(posts[position])
       prepareMoreImage(position+1)

        //---------------------
        if (position == posts.size - 2) {
            viewPager.post(run)
        }
        //------------------
    }

    private fun prepareMoreImage(position: Int) {
        var pos=position

         if (pos<posts.size){
                loadImage(pos)
         }else{
             pos=0
        }
        pos++
        if (pos<posts.size){
            loadImage(pos)
        }else{
            pos=0
        }

    }

    private fun loadImage(pos: Int) {

    }


    override fun getItemCount() = posts.size

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage = itemView?.findViewById<ImageView>(R.id.pagerImage)

        fun bindImage(post: Post) {
            val layout = itemView?.findViewById<ConstraintLayout>(R.id.itemLayout)
            drawPost.drawPost(post, layout)

            postImage.setOnClickListener {
                val intent = Intent(context, PostDetailsActivity::class.java)
               // intent.putExtra(DETAIL_POST_EXSTRA, post.postNum)

                intent.putExtra(POST_EXSTRA,post)

                context.startActivities(arrayOf(intent))

            }
        }

        fun bindImageCor(post: Post) {
            val layout = itemView?.findViewById<ConstraintLayout>(R.id.itemLayout)
            drawPost.drawPost(post, layout)

        }
    }


    //-------------------
    val run = object : Runnable {            // for automate scrolling
        override fun run() {
            posts.addAll(posts)
            notifyDataSetChanged()
        }
    }
    //-------------
}

