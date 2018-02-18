package com.kontus.cryptocurrencymanager.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.helpers.Config
import com.kontus.cryptocurrencymanager.helpers.CsvParser
import com.kontus.cryptocurrencymanager.helpers.SharedPreferencesHelper
import com.kontus.cryptocurrencymanager.interfaces.OnFragmentInteractionListener
import java.io.*

/**
 * Activities that contain this fragment must implement
 * [com.kontus.cryptocurrencymanager.interfaces.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DashboardFragment.newInstance] factory method to create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mInteractionListener: OnFragmentInteractionListener? = null
    private var mCSVTable: RelativeLayout? = null
    private var mLoadCSVButton: Button? = null

    private var mTableRowCount: Int? = null
    private var mTableColumnCount: Int? = null
    private var mTableCellValues: MutableList<Array<String>>? = null

    private var mShared: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

        mCSVTable = view?.findViewById(R.id.csv_table)
        mLoadCSVButton = view?.findViewById(R.id.load_csv)
        mLoadCSVButton?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(Intent.createChooser(intent, getString(R.string.load_csv_file)), Config.REQUEST_LOAD_CSV)
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mInteractionListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }

        mShared = SharedPreferencesHelper(context)
    }

    override fun onDetach() {
        super.onDetach()
        mInteractionListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_CANCELED -> {
                this.view?.let { view ->
                    Snackbar.make(view, getString(R.string.load_csv_file_canceled), Snackbar.LENGTH_LONG).show()
                }
            }
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Config.REQUEST_LOAD_CSV -> {
                        var path = data?.data?.path?.removePrefix("/document/raw:")

                        val csvParser = CsvParser()
                        val csvMetadata = csvParser.importCSV(File(path))

                        drawTable(csvMetadata)
                    }
                }
            }
        }
    }

    private fun drawTable(csvMetadata: CsvParser.CSVMetadata) {
        if (csvMetadata.fileLines == null || csvMetadata.rows == null || csvMetadata.columns == null) {

        } else {

        }

        val csvMetadata = csvMetadata?.let { it as? CsvParser.CSVMetadata } ?: return
        mTableRowCount = csvMetadata.rows
        mTableColumnCount = csvMetadata.columns
        mTableCellValues = csvMetadata.fileLines
        val numOfSelectedColumns = mShared?.selectedBittrexColumnsCSV?.size

        var indexesToKeep = mutableListOf<Int>()
        mShared?.selectedBittrexColumnsCSV?.forEach { it ->
            val index = mTableCellValues!![0].indexOf(it)
            if (index != -1) {
                indexesToKeep?.add(index)
            }
        }

        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        val cellWidth = displayWidth / numOfSelectedColumns!!

        val csvTableLayout = TableLayout(context)
        val csvTableLayoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val tableRowLayoutParams = TableRow.LayoutParams(cellWidth, 100)
        tableRowLayoutParams.setMargins(0, 2, 2, 0)

        for (tableRowIndex in 0 until mTableRowCount!!) {
            val tableRow = TableRow(context)
            tableRow.layoutParams = tableRowLayoutParams

            for (tableColumnIndex in 0 until mTableColumnCount!!) {
                // TODO save column indexes for each CSV in the shared preferences so we can hide columns that we don't want
                // if (tableColumnIndex == 0 || tableColumnIndex == 2) {
                if (!indexesToKeep.contains(tableColumnIndex)) {
                    continue
                }

                var color: Int = if (tableRowIndex == 0) {
                    Color.DKGRAY
                } else {
                    Color.LTGRAY
                }

                val cell = TextView(context)
                cell.setBackgroundColor(color)
                cell.gravity = Gravity.CENTER
                cell.text = mTableCellValues!![tableRowIndex][tableColumnIndex]
                tableRow.addView(cell, tableRowLayoutParams)
            }
            csvTableLayout.addView(tableRow, csvTableLayoutParams)
        }

        csvTableLayout.layoutParams = csvTableLayoutParams
        mCSVTable?.addView(csvTableLayout)
    }

    companion object {
        // fragment initialization parameters
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Factory method to create new instance of this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        fun newInstance(param1: String, param2: String): DashboardFragment {
            val fragment = DashboardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}