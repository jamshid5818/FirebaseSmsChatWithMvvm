package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jx.lessons.firebasesmschatwithmvvm.R
import jx.lessons.firebasesmschatwithmvvm.data.utils.SharedPref
import jx.lessons.firebasesmschatwithmvvm.data.utils.snackbar
import jx.lessons.firebasesmschatwithmvvm.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val sharedPref by lazy{
        SharedPref(this)
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        snackbar("Assalomu alekum ", binding.mainLayout)
        val navController = findNavController(R.id.main_nav_fragment)
        binding.bottomNavMenu.background = null
        binding.bottomNavMenu.menu.getItem(2).isEnabled = false
        binding.addPost.setOnClickListener {
            navController.navigate(R.id.postFragment)
        }
        binding.bottomNavMenu.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.miHome -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.miSearch -> {
                    navController.navigate(R.id.searchFragment)
                }
                R.id.miLikes -> {
                    navController.navigate(R.id.likeFragment)
                }
                R.id.miPerson -> {
                    if (!sharedPref.getEmail().toString().isEmpty()) {
                        navController.navigate(R.id.personFragment)
                    }else{
                        navController.navigate(R.id.registerFragment)
                    }
                }
            }
            true
        }
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.likeFragment,
                R.id.searchFragment,
                R.id.settingsFragment,
                R.id.personFragment,
                R.id.loginFragment,
                R.id.registerFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}