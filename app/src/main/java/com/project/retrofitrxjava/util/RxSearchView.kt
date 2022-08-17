package com.project.retrofitrxjava.util

import androidx.appcompat.widget.SearchView
import io.reactivex.rxjava3.subjects.PublishSubject

object RxSearchView {

    fun rxSearch(searchView: SearchView): PublishSubject<String> {
        val subject = PublishSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                subject.onNext(newText)
                return true
            }
        })

        return subject
    }

}