package com.lumu.snail

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.lumu.snail.categoriesfragment.CategoriesFragment
import com.lumu.snail.categoriesfragment.MyCategoriesRecyclerViewAdapter
import com.lumu.snail.chaptersfragment.ChaptersFragment
import com.lumu.snail.chaptersfragment.MyChaptersRecyclerViewAdapter
import com.lumu.snail.flashcardsActivity.FlashcardActivity
import com.lumu.snail.sage.GlossaryView
import com.lumu.snail.sage.SageActivity
import com.lumu.snail.tableOfContents.Category
import com.lumu.snail.tableOfContents.Subjects
import java.io.File

class MainActivity : AppCompatActivity(),
    MyCategoriesRecyclerViewAdapter.OnCategoryItemClickListener,
    MyChaptersRecyclerViewAdapter.OnChapterItemClickListener {

    private var currentTitle = "Home"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var actionBarMenu: Menu

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.run {
            putString("CURRENT_NAV_STATE", currentTitle)
        }
        super.onSaveInstanceState(savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topAppBar = this.findViewById(R.id.toolbar)
        topAppBar.title = currentTitle
        setSupportActionBar(topAppBar)


        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    // Handle edit text press
                    true
                }
                R.id.favorite -> {
                    // Handle favorite icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (currentTitle == "Home") {
                        // If already on the CategoriesFragment, call the default back behavior (exit the app)
                        finish()
                    } else {
                        // Otherwise, go back to the CategoriesFragment and update the title
                        currentTitle = "Home"
                        replaceFragmentContainer(R.id.fragmentContainerView, CategoriesFragment())
                        /*btnBack.animate().rotationXBy(-90f)
                            .setDuration(400)
                            .setInterpolator(LinearInterpolator())
                            .withEndAction {
                                btnBack.visibility = View.INVISIBLE
                            }
                            .start()
                            */
                    }
                }
            }

        // Add the callback to the activity's back stack
        onBackPressedDispatcher.addCallback(this, callback)


        if (savedInstanceState != null) {
            currentTitle =
                savedInstanceState.getString("CURRENT_NAV_STATE", currentTitle) ?: "Home"
        }

        if (currentTitle == "Home") {
            replaceFragmentContainer(R.id.fragmentContainerView, CategoriesFragment())
        } else {
            onCategoryItemClick(Category.valueOf(currentTitle))
        }

        /*
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, you can access external storage
            val flashcardsDir = File(Environment.getExternalStorageDirectory(), "flashcards")
            // Use flashcardsDir for further operations
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
        */

        // Find the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCategoryItemClick(category: Category) {
        // Update the title with the selected category name
        currentTitle = category.toString()

        //val btnBack = this@MainActivity.findViewById<ImageButton>(R.id.btn_back)
        val appBar = this@MainActivity.findViewById<MaterialToolbar>(R.id.toolbar)
        appBar.setTitle(currentTitle)
        /*
        btnBack.animate()
            .withStartAction {
                btnBack.rotationX = 90F
                btnBack.visibility = View.VISIBLE
            }
            .withEndAction{
            btnBack.animate()
                .rotationXBy(90F)
                .setDuration(200)
                .setInterpolator(LinearInterpolator())
                .start()
            }
            .start()
         */
        // Replace the current fragment with a new ChaptersFragment for the selected category
        val fragment = ChaptersFragment.newInstance(category)
        replaceFragmentContainer(R.id.fragmentContainerView, fragment)
    }

    override fun onChapterItemClick(chapter: String, category: Category) {
        if (category.toString() == "Sage") {
            if (chapter == "(Incomplete) Glossary") {
                startActivity(
                    GlossaryView.newIntent(this@MainActivity)
                )
            }
            else {
                startActivity(
                    SageActivity.newIntent(this@MainActivity, chapter),
                    ActivityOptions.makeSceneTransitionAnimation(
                        this@MainActivity
                    ).toBundle()
                )
            }
        }
        else{
            // Start the FlashcardActivity for the selected chapter
            startActivity(
                FlashcardActivity.newIntent(this@MainActivity, chapter),
                ActivityOptions.makeSceneTransitionAnimation(
                    this@MainActivity).toBundle()
            )
        }
    }

    fun replaceFragmentContainer(oldFragment: Int, newFragment: Fragment) {
        //this.findViewById<TextView>(R.id.textView).text = currentTitle

        // Get the FragmentManager
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the default fragment container with the list fragment
        fragmentTransaction.replace(oldFragment, newFragment)

        // Add the current fragment to the back stack
        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }

    fun getFlashcardsData(){
        val flashcardsDir = File(applicationContext.filesDir, "Flashcards")
        if (!flashcardsDir.exists()) {
            flashcardsDir.mkdir()
        }
        if (flashcardsDir.exists() && flashcardsDir.isDirectory){
            for (filesOrFolders in flashcardsDir.listFiles()){
                if (filesOrFolders.isDirectory){
                    var folderName = filesOrFolders.name
                    Subjects.addNewSubject(folderName, getFlashcardsfromFolder(folderName, flashcardsDir))
                }
            }
        }
    }

    private fun getFlashcardsfromFolder(foldername: String, flashcardsDir: File): MutableList<String>{
        val subjectDir = File(flashcardsDir, foldername)

        var fileNames = mutableListOf<String>()

        if (subjectDir.exists() && subjectDir.isDirectory) {
            // Get a list of files and folders inside the "flashcards" directory
            val filesAndFolders: Array<out File>? = subjectDir.listFiles()

            // Loop through the array to differentiate between files and folders
            if (filesAndFolders != null) {
                for (fileOrFolder in filesAndFolders) {
                    if (fileOrFolder.isFile) {
                        fileNames.add(fileOrFolder.name)
                        // Do something with the file name
                    }
                }
            }
        }
        return fileNames
    }
}