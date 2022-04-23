package com.sg.pager25.post_activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sg.pager25.R
import com.sg.pager25.adapters.CommentAdapter
import com.sg.pager25.databinding.ActivityPostDetailsBinding
import com.sg.pager25.interfaces.CommentsOptionClickListener
import com.sg.pager25.models.Comment
import com.sg.pager25.models.Post
import com.sg.pager25.utilities.Constants.COMMEND_TIME_STAMP
import com.sg.pager25.utilities.Constants.COMMENT_ID
import com.sg.pager25.utilities.Constants.COMMENT_LIST
import com.sg.pager25.utilities.Constants.COMMENT_POST_ID
import com.sg.pager25.utilities.Constants.COMMENT_REF
import com.sg.pager25.utilities.Constants.COMMENT_TEXT
import com.sg.pager25.utilities.Constants.CURRENT_USER_EXIST
import com.sg.pager25.utilities.Constants.DETAIL_POST_EXSTRA
import com.sg.pager25.utilities.Constants.EXIST
import com.sg.pager25.utilities.Constants.POST_REF
import com.sg.pager25.utilities.Constants.SHAR_PREF
import com.sg.pager25.utilities.UtilityPost
import java.util.ArrayList

class PostDetailsActivity : AppCompatActivity(), CommentsOptionClickListener {
    lateinit var binding:ActivityPostDetailsBinding

    private var currentUser: FirebaseUser? = null
    var util = UtilityPost()
    var textViewArray = ArrayList<TextView>()
    lateinit var commentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    val comments = ArrayList<Comment>()
    var currentPostNum = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostDetailsBinding.inflate(layoutInflater)
        createTextViewArray()
        setContentView(binding.root)
        currentPostNum = intent.getIntExtra(DETAIL_POST_EXSTRA, 0)

        create_commentsRv()
        operateButtoms()
        createComments()
        findCurrentPost()

    }

    private fun operateButtoms() {
        /* binding.signUpBtn.setOnClickListener {
             startActivity(Intent(this, SignUpActivity::class.java))
         }*/
        binding.signInBtn.setOnClickListener {
            //startActivity(Intent(this, LoginActivity::class.java))
            startActivity(Intent(this,PostRegisterActivity::class.java))
        }

        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, AccountPostSettingActivity::class.java))
        }

        val textWatcher=object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //util.logi("PostDetails  102                            currentUser=$currentUser")
                if (currentUser==null){
                    hideKeyboard()
                    util.createDialog(this@PostDetailsActivity, 2)
                }
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun afterTextChanged(p0: Editable?) {  }
        }

        val profileC= binding.profileImageComment
        val textC= binding.postCommentText
        textC.addTextChangedListener(textWatcher)

        profileC.setOnClickListener {
            util.logi("PostDetails  109                             currentUser=$currentUser")
            addComment()
        }
        textC.setOnClickListener {
            util.logi("PostDetails  110                               currentUser=$currentUser")

            addComment()
        }


    }

    override fun onStart() {
        super.onStart()
        //   util.logi("PostDetails 121 in OnStart")
        val pref = getSharedPreferences(SHAR_PREF, Context.MODE_PRIVATE)
        val existUser = pref.getString(CURRENT_USER_EXIST, "none").toString()
        // util.logi("PostDetails 122 in OnStart          existUse1=$existUser")
        if (existUser == EXIST) {
            currentUser = FirebaseAuth.getInstance().currentUser
            binding.nameCurrentUserName.setText(currentUser!!.displayName.toString())
        }
    }

    private fun createComments() {
        FirebaseFirestore.getInstance().collection(COMMENT_REF).document(currentPostNum.toString())
            .collection(COMMENT_LIST)
            .orderBy(COMMEND_TIME_STAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    comments.clear()
                    for (doc in value.documents) {
                        val comment = util.retriveCommentFromFirestore(doc)
                        comments.add(comment)
                    }
                    //    util.logi("PostDetailsActivity 111        comments.size=${comments.size} ")
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
                    .document(currentPostNum.toString())
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


    private fun findCurrentPost() {
        FirebaseFirestore.getInstance().collection(POST_REF).document(currentPostNum.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val post = util.retrivePostFromFirestore(task.result)
                    drawHeadline(post)
                }
            }
    }


    private fun drawHeadline(post: Post) {
        val num = post.postNum
        val st = "   פוסט מספר: " + "$num   "
        binding.postNumber.text = st
        // util.logi("PostDetailsActivity  111  post=$post     \n post.postText.size= ${post.postText.size}")
        for (ind in 0 until post.postText.size) {

            //   util.logi("PostDetailsActivity  112  ind=$ind     \n")

            textViewArray[ind].visibility = View.VISIBLE
            textViewArray[ind].text = post.postText[ind]
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