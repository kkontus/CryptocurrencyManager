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
import com.kontus.cryptocurrencymanager.interfaces.OnFragmentInteractionListener
import java.io.*

/**
 * Activities that contain this fragment must implement the
 * [DashboardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    private var mCSVTable: RelativeLayout? = null
    private var mLoadCSV: Button? = null

    private var tableRowCount = 3
    private var tableColumnCount = 3
    private var tableCellValues = arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6), intArrayOf(7, 8, 9))

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
        mLoadCSV = view?.findViewById(R.id.load_csv)
        mLoadCSV?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(Intent.createChooser(intent, "Open CSV"), Config.REQUEST_LOAD_CSV)
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_CANCELED -> {
                this.view?.let { view ->
                    Snackbar.make(view, "Canceled opening csv file", Snackbar.LENGTH_LONG).show()
                }
            }
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Config.REQUEST_LOAD_CSV -> {
                        var path = data?.data?.path?.removePrefix("/document/raw:")

                        val csvParser = CsvParser()
                        csvParser.importCSV(File(path))

                        drawTable()
                    }
                }
            }
        }
    }

    private fun drawTable() {
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        val cellWidth = displayWidth / tableColumnCount

        val csvTableLayout = TableLayout(context)
        val csvTableLayoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val tableRowLayoutParams = TableRow.LayoutParams(cellWidth, 100)
        tableRowLayoutParams.setMargins(5, 5, 0, 0)

        for (tableRowIndex in 0 until tableRowCount) {
            val tableRow = TableRow(context)
            tableRow.layoutParams = tableRowLayoutParams

            for (tableColumnIndex in 0 until tableColumnCount) {
                var color: Int = if (tableRowIndex == 0) {
                    Color.DKGRAY
                } else {
                    Color.LTGRAY
                }

                val cell = TextView(context)
                cell.setBackgroundColor(color)
                cell.gravity = Gravity.CENTER

                cell.text = tableCellValues[tableRowIndex][tableColumnIndex].toString()
                tableRow.addView(cell, tableRowLayoutParams)

            }
            csvTableLayout.addView(tableRow, csvTableLayoutParams)
        }

        csvTableLayout.layoutParams = csvTableLayoutParams
        mCSVTable?.addView(csvTableLayout)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
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