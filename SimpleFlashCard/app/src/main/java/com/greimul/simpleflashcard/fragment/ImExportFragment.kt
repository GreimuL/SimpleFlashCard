package com.greimul.simpleflashcard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.fragment_im_export.*
import kotlinx.android.synthetic.main.fragment_im_export.view.*

class ImExportFragment(val deckViewModel: DeckViewModel): Fragment() {

    var isImportExpand:Boolean = false
    var isExportExpand:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_im_export,container,false)

        val fragmentManager = getFragmentManager()
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        var expandImgArray = arrayOf(
            view.context.resources.getDrawable(R.drawable.ic_expand_more_48px,null),
            view.context.resources.getDrawable(R.drawable.ic_expand_less_48px,null)
        )

        view.cardview_import.setOnClickListener {
            if(isImportExpand) {
                isImportExpand = false
                view.textview_cardview_import.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    expandImgArray[0],
                    null
                )
            }
            else{
                isImportExpand = true
                view.textview_cardview_import.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    expandImgArray[1],
                    null
                )

                fragmentTransaction.replace(R.id.framelayout,ImportFragment(deckViewModel))

                if (isExportExpand) {
                    isExportExpand = false
                    view.textview_cardview_export.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        expandImgArray[0],
                        null
                    )
                }
            }
        }
        view.cardview_export.setOnClickListener {
            if(isExportExpand) {
                isExportExpand = false
                view.textview_cardview_export.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    expandImgArray[0],
                    null
                )
            }
            else{
                isExportExpand = true
                view.textview_cardview_export.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    expandImgArray[1],
                    null
                )
                if (isImportExpand) {
                    isImportExpand = false
                    view.textview_cardview_import.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        expandImgArray[0],
                        null
                    )
                }
            }
        }

        return view
    }

}