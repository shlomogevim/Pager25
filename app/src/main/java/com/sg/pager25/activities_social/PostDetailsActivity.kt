package com.sg.pager25.activities_social

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sg.pager25.R
import com.sg.pager25.adapters.CommentAdapter
import com.sg.pager25.databinding.ActivityPostDetailsBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.general.BaseActivity
import com.sg.pager25.interfaces.CommentsOptionClickListener
import com.sg.pager25.login.activities_appshop.LoginActivity
import com.sg.pager25.login.activities_appshop.SettingActivity
import com.sg.pager25.models.Comment
import com.sg.pager25.models.Post
import com.sg.pager25.models.User
import com.sg.pager25.utilities.Constants.COMMEND_TIME_STAMP
import com.sg.pager25.utilities.Constants.COMMENT_ID
import com.sg.pager25.utilities.Constants.COMMENT_LIST
import com.sg.pager25.utilities.Constants.COMMENT_POST_ID
import com.sg.pager25.utilities.Constants.COMMENT_REF
import com.sg.pager25.utilities.Constants.COMMENT_TEXT
import com.sg.pager25.utilities.Constants.POST_EXSTRA
import com.sg.pager25.utilities.Constants.POST_REF
import com.sg.pager25.utilities.Constants.USER_EXTRA
import com.sg.pager25.utilities.UtilityPost
import java.util.ArrayList

class PostDetailsActivity : BaseActivity(), CommentsOptionClickListener {
    lateinit var binding:ActivityPostDetailsBinding

    private var currentUser:User?= null
    var util = UtilityPost()
    var textViewArray = ArrayList<TextView>()
    lateinit var commentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    val comments = ArrayList<Comment>()
    //var currentPostNum = 0
    lateinit var currentPost:Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostDetailsBinding.inflate(layoutInflater)
        createTextViewArray()
        setContentView(binding.root)

        if (intent.hasExtra(POST_EXSTRA)) {
            currentPost = intent.getParcelableExtra(POST_EXSTRA)!!
        }
      //  logi("PostDetailActivity  68          currentPost===>> $currentPost  /n")

       create_commentsRv()
         operateButtoms()
        createComments()
    }

    override fun onStart() {
        super.onStart()
        FirestoreClass().getUserDetails(this)

    }
    private fun operateButtoms() {
        /* binding.signUpBtn.setOnClickListener {
             startActivity(Intent(this, SignUpActivity::class.java))
         }*/
        binding.signInBtn.setOnClickListener {
            //startActivity(Intent(this, LoginActivity::class.java))
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.profileBtn.setOnClickListener {
       //  logi("PostDetaileActivity  92   =====> /n  currentPost=$currentPost ")
//            val intent=Intent(this,AccountPostSettingActivity::class.java)
            val intent=Intent(this, SettingActivity::class.java)
            intent.putExtra(USER_EXTRA,currentUser)
          startActivity(intent)
        }


//
//        val textWatcher=object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                //util.logi("PostDetails  102                            currentUser=$currentUser")
//                if (currentUser==null){
//                    hideKeyboard()
//                    util.createDialog(this@PostDetailsActivity, 2)
//                }
//            }
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
//            override fun afterTextChanged(p0: Editable?) {  }
//        }
//
//        val profileC= binding.profileImageComment
//        val textC= binding.postCommentText
//        textC.addTextChangedListener(textWatcher)
//
//        profileC.setOnClickListener {
//            util.logi("PostDetails  109                             currentUser=$currentUser")
//            addComment()
//        }
//        textC.setOnClickListener {
//            util.logi("PostDetails  110                               currentUser=$currentUser")
//
//            addComment()
//        }

    }

     fun getUserName(user: User) {
        currentUser=user
        drawUserName()
    }

    fun  drawUserName(){
//        logi("PostDetailsActivity 232   currentUser= $currentUser")
        if (currentUser==null){
            binding.nameCurrentUserName.setText("אנונימי")
        }else{
            binding.nameCurrentUserName.setText("${currentUser!!.firstName}")
        }
    }

    private fun drawHeadline() {
        val num = currentPost.postNum
        val st = "   פוסט מספר: " + "$num   "
        binding.postNumber.text = st
        //logi("PostDetailsActivity  111  post=$post     \n post.postText.size= ${post.postText.size}")
        for (ind in 0 until currentPost.postText.size) {
            //logi("PostDetailsActivity  112  ind=$ind     \n")
            textViewArray[ind].visibility = View.VISIBLE
            textViewArray[ind].text = currentPost.postText[ind]
        }
    }

    private fun createComments() {
      //  logi(" PostDetail 124")
        FirebaseFirestore.getInstance().collection(COMMENT_REF).document(currentPost.postNum.toString())
            .collection(COMMENT_LIST)
            .orderBy(COMMEND_TIME_STAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    comments.clear()
                    for (doc in value.documents) {
                        val comment = util.retriveCommentFromFirestore(doc)
                        comments.add(comment)
                    }
                 //  logi("PostDetailsActivity 135        comments.size=${comments.size} ")
                    commentAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun addComment() {
        util.logi("PostDetails  112                               currentUser=$currentUser")
        if (currentUser == null) {
            //   util.logi("PostDetails  113                               currentUser=$currentUser")
            hideKeyboard()
            util.createDialog(this, 1)
        } else {
            util.logi("PostDetails  114                               currentUser=$currentUser")
            val commentText = binding.postCommentText.text.toString()
            if (commentText == "") {
                // util.toasti(this, " היי , קודם תכתוב משהו בהערה ואחר כך תלחץ ...")
                util.createDialog(this, 3)
            } else {
                binding.postCommentText.text.clear()
                hideKeyboard()
                FirebaseFirestore.getInstance().collection(POST_REF)
                    .document(currentPost.postNum.toString())
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val post = util.retrivePostFromFirestore(task.result)
                            util.createComment(post, commentText)
                        }
                    }
            }
        }
    }



    private fun create_commentsRv() {
        commentsRV = binding.rvPost
        commentAdapter = CommentAdapter(comments, this)
        val layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        commentsRV.layoutManager = layoutManger
        commentsRV.adapter = commentAdapter
    }


    private fun createTextViewArray() {
        with(binding) {
            textViewArray = arrayListOf(
                tvPost1,
                tvPost2,
                tvPost3,
                tvPost4,
                tvPost5,
                tvPost6,
                tvPost7,
                tvPost8,
                tvPost9
            )
        }

    }

    private fun hideKeyboard() {
        val inputeManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputeManager.isAcceptingText) {
            inputeManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun optionMenuClicked(comment: Comment) {

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.option_menu, null)
        val deleteBtn = dialogView.findViewById<Button>(R.id.optionDelelBtn)
        val editBtn = dialogView.findViewById<Button>(R.id.optionEditBtn)
        builder.setView(dialogView)
            .setNegativeButton("Cancel") { _, _ -> }
        val ad = builder.show()
        deleteBtn.setOnClickListener {
            util.deleteComment(comment)
            finish()
        }
        editBtn.setOnClickListener {
            val intent = Intent(this, UpdateCommentActivity::class.java)
            intent.putExtra(COMMENT_POST_ID, comment.postId)
            intent.putExtra(COMMENT_ID, comment.commntId)
            intent.putExtra(COMMENT_TEXT, comment.text)
            startActivity(intent)
            finish()
        }
    }




}