package com.kamontat.checkidnumber.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kamontat.checkidnumber.R
import com.kamontat.checkidnumber.adapter.ViewPagerAdapter
import com.kamontat.checkidnumber.api.Logger
import com.kamontat.checkidnumber.api.constants.Status
import com.kamontat.checkidnumber.api.getter.About
import com.kamontat.checkidnumber.api.getter.Export
import com.kamontat.checkidnumber.api.getter.NonExport
import com.kamontat.checkidnumber.model.IDNumber
import com.kamontat.checkidnumber.model.Pool
import com.kamontat.checkidnumber.presenter.MainPresenter
import com.kamontat.checkidnumber.view.fragment.InputFragment
import java.util.*

class MainActivity : AppCompatActivity(), MainView {
    // private val TAG = "MainActivity"
    private var presenter: MainPresenter? = null
    private var header: String? = null

    private var navigation: BottomNavigationView? = null
    var viewPager: ViewPager? = null
        private set

    private var inputFragment: InputFragment? = null
    private var listFragment: ListFragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener listener@ { item ->
        when (item.itemId) {
            R.id.navigation_insert -> {
                viewPager!!.currentItem = 0
                item.isChecked = true
                showKeyBoard()
                return@listener true
            }
            R.id.navigation_list -> {
                viewPager!!.currentItem = 1
                item.isChecked = true
                hideKeyBoard()
                return@listener true
            }
        }
        false
    }

    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            for (i in 0..navigation!!.menu.size() - 1)
                navigation!!.menu.getItem(i).isChecked = false
            navigation!!.menu.getItem(position).isChecked = true
            if (navigation!!.menu.getItem(position).itemId == R.id.navigation_insert)
                showKeyBoard()
            else if (navigation!!.menu.getItem(position).itemId == R.id.navigation_list) hideKeyBoard()
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, Pool(this))
        header = resources.getString(R.string.input_message)

        viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager!!.addOnPageChangeListener(mOnPageChangeListener)

        navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        setViewPaperAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        toggleExportFeatureMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.top_menu_export -> if (requestPermission()) presenter?.let { Export(it).show() }
            R.id.top_menu_about -> About(this).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        toggleExportFeatureMenu(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.PERMISSION_CODE -> for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    NonExport(this).show()
                } else {
                    if (checkPermission())
                        presenter?.let { Export(it).show() }
                    else
                        EXPORT_FEATURE = false
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Logger.Log(this.javaClass.name, {
            Log.i(it, "start saving")
        })
        super.onSaveInstanceState(outState)
        outState?.putSerializable(STATE_CONSTANT_PRESENTER, presenter)
        // outState?.putSerializable(STATE_CONSTANT_PRESENTER, inputFragment)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Logger.Log(this.javaClass.name, {
            Log.i(it, "start loading")
        })
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.getSerializable(STATE_CONSTANT_PRESENTER)
    }

    private fun setViewPaperAdapter() {
        inputFragment = InputFragment()
        inputFragment!!.setButton(inputOnClick)
        inputFragment!!.setInputListener(inputTextChange)

        listFragment = ListFragment()
        listFragment!!.listAdapter = presenter!!.pool

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(inputFragment!!)
        adapter.addFragment(listFragment!!)
        viewPager!!.adapter = adapter
    }

    private val inputTextChange: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                updateInput()
            }
        }

    private val inputOnClick: View.OnClickListener
        get() = View.OnClickListener { checkAndInsert() }

    private fun updateInput() {
        updateInput(IDNumber(inputText))
    }

    override fun checkAndInsert() {
        val id = IDNumber(inputText)
        if (id.status === Status.OK) {
            presenter!!.addID(id)
            inputFragment!!.clearText()
        } else
            updateInput(id)
    }

    override fun updateInput(id: IDNumber) {
        Logger.Log(this.javaClass.name, {
            Log.i(it, id.status.toString())
        })

        inputFragment!!.updateInputSize(id.size)
        inputFragment!!.setButtonEnable(id.status === Status.OK)
        inputFragment!!.updateStatus(id.status, resources)
    }

    override fun hideKeyBoard() {
        inputFragment!!.hideKeyBoard(this)
    }

    override fun showKeyBoard() {
        inputFragment!!.showKeyboard(this)
    }

    override val inputText: String
        get() = inputFragment!!.getInput()

    override val isStorageWritable: Boolean
        get() = isExternalStorageWritable

    override val idNumbers: Array<IDNumber>
        get() = presenter!!.idNumbers

    override val context: Context
        get() = this

    /**
     * Checks if external storage is available for read and write
     */
    val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    /**
     * Checks if external storage is available to at least read
     */
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    override fun checkPermission(): Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @return `true`, only have permission; otherwise return `false`
     */
    override fun requestPermission(): Boolean {
        // Here, thisActivity is the current activity
        when {
            !checkPermission() -> {
                // Should we show an explanation?
                when {
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) -> NonExport(this).show()
                    else -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.PERMISSION_CODE)
                }
                return false
            }
            else -> return true
        }
    }

    fun toggleExportFeatureMenu(menu: Menu) {
        val title = String.format(Locale.ENGLISH, "%s %d id (%s)", resources.getString(R.string.export), presenter!!.pool.count, resources.getString(R.string.xls))
        menu.findItem(R.id.top_menu_export).isVisible = EXPORT_FEATURE
        menu.findItem(R.id.top_menu_export).title = title
        menu.findItem(R.id.top_menu_export).isEnabled = presenter!!.pool.count > 0
    }

    companion object {
        var EXPORT_FEATURE = true
        val STATE_CONSTANT_PRESENTER: String = "presenter"
    }

    object Constants {
        val PERMISSION_CODE = 1
    }
}