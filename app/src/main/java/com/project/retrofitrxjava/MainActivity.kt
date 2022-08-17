package com.project.retrofitrxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.project.retrofitrxjava.data.paging.TaskPagingSource
import com.project.retrofitrxjava.data.repository.paging.TaskRepositoryPagingImpl
import com.project.retrofitrxjava.data.response.TaskResponse
import com.project.retrofitrxjava.databinding.ActivityMainBinding
import com.project.retrofitrxjava.ui.TaskViewModel
import com.project.retrofitrxjava.ui.adapter.TaskAdapter
import com.project.retrofitrxjava.ui.adapter.TaskPagingAdapter
import com.project.retrofitrxjava.ui.paging.TaskPagingViewModel
import com.project.retrofitrxjava.util.RxSearchView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mSearchView: SearchView
    private lateinit var progressBar: ProgressBar

    //private lateinit var viewModel: TaskViewModel
    private lateinit var viewModelPaging: TaskPagingViewModel
    private lateinit var fabAddTask: ExtendedFloatingActionButton
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mTaskAdapter: TaskAdapter
    private lateinit var taskPagingAdapter: TaskPagingAdapter
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val editClickListener: (data: TaskResponse.TaskResponseItem) -> Unit = {
        editTaskDialog(it)
    }

    private val deleteClickListener: (data: TaskResponse.TaskResponseItem) -> Unit = {
        deleteTaskDialog(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //inint view model
        /*viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(TaskViewModel::class.java)*/
        val taskPagingSource: TaskPagingSource =
            TaskPagingSource((application as MyApplication).networkService)
        val repository = TaskRepositoryPagingImpl(taskPagingSource)
        viewModelPaging = ViewModelProvider(
            this, TaskPagingViewModelFactory(repository)
        )
            .get(TaskPagingViewModel::class.java)
        //adapter for paging data
        taskPagingAdapter = TaskPagingAdapter()
        //find resource by id
        progressBar = findViewById(R.id.pbTask)
        fabAddTask = findViewById(R.id.fabAddTask)
        mRecyclerView = findViewById(R.id.rvTask)
        //setup task adapter
        mTaskAdapter = TaskAdapter(editClickListener, deleteClickListener)
        //setup recyclerview
        mRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = taskPagingAdapter
        }

        //show add task dialog
        fabAddTask.setOnClickListener {
            addTaskDialog()
        }

        taskPagingAdapter.addLoadStateListener {
            binding.pbTask.isVisible = it.source.refresh is LoadState.Loading
            binding.rvTask.isVisible = it.source.refresh is LoadState.NotLoading

            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.source.refresh as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error
                ?: it.refresh as? LoadState.Error

            errorState?.let {
                Toast.makeText(this, "Error: ${it.error}", Toast.LENGTH_LONG).show()
            }

        }

        observers()
    }

    //observe live data
    private fun observers() {

        viewModelPaging.getAllTaskPaging()
            .subscribe {
                taskPagingAdapter.submitData(lifecycle, it)
            }
/*        viewModel.isLoading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE

        })

        viewModel.isError.observe(this, Observer {


        })

        viewModel.isSuccess.observe(this, Observer {
            successDialog()
        })

        viewModel.isEditSuccess.observe(this, Observer {
            successEditDialog()

        })

        viewModel.isDeleted.observe(this, Observer {
            deletedDialog()

        })

        viewModel.taskList.observe(this, Observer {
            mTaskAdapter.setTaskList(it)
        })*/
    }

    private fun addTaskDialog() {
        val dialog = MaterialDialog(this)
            .cornerRadius(8f)
            .cancelable(false)
            .customView(R.layout.add_task_dialog)

        val customView = dialog.getCustomView()

        val title = customView.findViewById<TextInputEditText>(R.id.addTitle)
        val body = customView.findViewById<TextInputEditText>(R.id.addBody)
        val note = customView.findViewById<TextInputEditText>(R.id.addNote)
        val status = customView.findViewById<TextInputEditText>(R.id.addStatus)

        dialog.positiveButton(R.string.dialog_btn_save) {
/*            viewModel.mUserId.value = "2"
            viewModel.mTitle.value = title.text.toString()
            viewModel.mBody.value = body.text.toString()
            viewModel.mNote.value = note.text.toString()
            viewModel.mStatus.value = status.text.toString()

            viewModel.addTask()*/

        }
        dialog.negativeButton {
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun editTaskDialog(data: TaskResponse.TaskResponseItem) {
        /*  val dialog = MaterialDialog(this)
              .cornerRadius(8f)
              .cancelable(false)
              .customView(R.layout.add_task_dialog)

          val customView = dialog.getCustomView()

          val headTitle = customView.findViewById<TextView>(R.id.lbAddTask)
          val title = customView.findViewById<TextInputEditText>(R.id.addTitle)
          val body = customView.findViewById<TextInputEditText>(R.id.addBody)
          val note = customView.findViewById<TextInputEditText>(R.id.addNote)
          val status = customView.findViewById<TextInputEditText>(R.id.addStatus)

          headTitle.text = getString(R.string.edit_task)
          viewModel.mTaskId.value = data.id.toLong()
          title.setText(data.title)
          body.setText(data.body)
          note.setText(data.note)
          status.setText(data.status)

          dialog.positiveButton(R.string.dialog_btn_save) {
              viewModel.mUserId.value = "2"
              viewModel.mTitle.value = title.text.toString()
              viewModel.mBody.value = body.text.toString()
              viewModel.mNote.value = note.text.toString()
              viewModel.mStatus.value = status.text.toString()

              viewModel.editTask()

          }
          dialog.negativeButton {
              dialog.dismiss()

          }
          dialog.show()*/
    }

    private fun deleteTaskDialog(data: TaskResponse.TaskResponseItem) {
        val dialog = MaterialDialog(this)
            .cornerRadius(8f)
            .cancelable(false)
            .title(R.string.dialog_delete_task)
            .message(null, "${getString(R.string.dialog_delete_msg)} ${data.title} ?")

        dialog.positiveButton(R.string.dialog_btn_delete) {
/*            viewModel.mTaskId.value = data.id.toLong()
            viewModel.mUserId.value = "2"

            viewModel.deleteTask()*/

        }
        dialog.negativeButton {
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun successDialog() {
        val dialog = MaterialDialog(this)
            .cornerRadius(8f)
            .cancelable(false)
            .title(R.string.dialog_success_title)
            .message(null, "${getString(R.string.dialog_success_msg)} ")


        dialog.positiveButton {
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun successEditDialog() {
        val dialog = MaterialDialog(this)
            .cornerRadius(8f)
            .cancelable(false)
            .title(R.string.dialog_success_title)
            .message(null, "${getString(R.string.dialog_editsuccess_msg)} ")


        dialog.positiveButton {
            dialog.dismiss()

        }
        dialog.show()
    }


    private fun deletedDialog() {
        val dialog = MaterialDialog(this)
            .cornerRadius(8f)
            .cancelable(false)
            .title(R.string.dialog_success_title)
            .message(null, "${getString(R.string.dialog_deleted_task_msg)} ")


        dialog.positiveButton {
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun searchView() {
/*        RxSearchView.rxSearch(mSearchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter {
                return@filter !it.isNullOrEmpty()
            }
            .distinctUntilChanged()
            .switchMapSingle { quert ->
                viewModel.searchTask(quert)
            }
            .subscribe(
                {
                    viewModel.taskList.value = it
                },
                {
                    Log.d(TAG, "onError: $it")

                },
                {
                    Log.d(TAG, "onComplete")
                }
            )*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        mSearchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
/*
        when (item.itemId) {
            R.id.clear_filter ->
                viewModel.getAllTask()

        }
*/

        return super.onOptionsItemSelected(item)
    }

    class TaskPagingViewModelFactory(private val repository: TaskRepositoryPagingImpl) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass
                .getConstructor(TaskRepositoryPagingImpl::class.java)
                .newInstance(repository)
        }

    }

}