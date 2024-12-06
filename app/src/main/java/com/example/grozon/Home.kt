package com.example.grozon

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var generateExcelButton: View
    private val selectedItems = mutableListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home2, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        generateExcelButton = view.findViewById(R.id.generateExcelButton)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Grocery data with categories
        val groceryCategories = listOf(
            GroceryCategory("Grains & Staples", listOf("Rice", "Wheat", "Flour", "Oats", "Pasta", "Quinoa", "Barley")),
            GroceryCategory("Legumes & Pulses", listOf("Lentils", "Chickpeas", "Kidney beans", "Black beans", "Split peas")),
            GroceryCategory("Dairy & Eggs", listOf("Milk", "Cheese", "Butter", "Yogurt", "Eggs")),
            GroceryCategory("Fruits", listOf("Apples", "Bananas", "Oranges", "Grapes", "Pineapple", "Mangoes", "Berries", "Avocados")),
            GroceryCategory("Vegetables", listOf("Potatoes", "Onions", "Tomatoes", "Carrots", "Spinach", "Lettuce", "Bell Peppers", "Cauliflower", "Cucumbers", "Garlic", "Ginger")),
            GroceryCategory("Beverages", listOf("Tea", "Coffee", "Fruit Juices", "Soft Drinks", "Bottled Water", "Coconut Water")),
            GroceryCategory("Oils & Vinegars", listOf("Vegetable Oil", "Olive Oil", "Coconut Oil", "Vinegar", "Ghee")),
            GroceryCategory("Spices & Condiments", listOf("Salt", "Black Pepper", "Cumin", "Turmeric", "Red Chili Powder", "Garam Masala", "Curry Powder", "Mustard", "Soy Sauce", "Ketchup", "Mayonnaise", "Honey")),
            GroceryCategory("Snacks", listOf("Chips", "Biscuits", "Nuts", "Dried Fruits", "Popcorn", "Granola Bars")),
            GroceryCategory("Frozen Foods", listOf("Frozen Vegetables", "Frozen Berries", "Frozen Pizza", "Ice Cream", "Frozen Meats")),
            GroceryCategory("Baking Ingredients", listOf("Sugar", "Baking Powder", "Baking Soda", "Yeast", "Chocolate Chips", "Vanilla Extract")),
            GroceryCategory("Canned & Jarred Goods", listOf("Canned Beans", "Canned Vegetables", "Canned Soups", "Canned Tomatoes", "Canned Fruits")),
            GroceryCategory("Personal Care & Household", listOf("Toilet Paper", "Dish Soap", "Laundry Detergent", "Hand Soap", "Toothpaste", "Shampoo", "Cleaning Supplies", "Paper Towels", "Trash Bags"))
        )



        recyclerView.adapter = GroceryAdapter(groceryCategories)

        // Generate Excel file on button click
        generateExcelButton.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(context, "Please select items to create the file", Toast.LENGTH_SHORT).show()
            } else {
                createExcelFile()
            }
        }

        return view
    }

    private fun createExcelFile() {
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Selected Items")

            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Item Name")
            headerRow.createCell(1).setCellValue("Count")
            headerRow.createCell(2).setCellValue("Unit")

            selectedItems.forEachIndexed { index, item ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(item)
                row.createCell(1).setCellValue("") // Empty cell
                row.createCell(2).setCellValue("") // Empty cell
            }

            val timestamp = System.currentTimeMillis()
            val fileName = "SelectedItems_$timestamp.xlsx"
            val file = File(requireContext().getExternalFilesDir(null), fileName)

            val externalPath = "${Environment.getExternalStorageDirectory().absolutePath}/Download/$fileName"


            FileOutputStream(file).use { workbook.write(it) }
            FileOutputStream(externalPath).use { workbook.write(it) }
            workbook.close()
            Toast.makeText(activity, "Excel exported to ${file.absolutePath} and $externalPath", Toast.LENGTH_LONG).show()

//            Toast.makeText(context, "Excel file created at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to create Excel file", Toast.LENGTH_SHORT).show()
        }
    }

    data class GroceryCategory(val name: String, val items: List<String>)

    inner class GroceryAdapter(private val categories: List<GroceryCategory>) :
        RecyclerView.Adapter<GroceryAdapter.CategoryViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_with_checkbox, parent, false)
            return CategoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.bind(categories[position])
        }

        override fun getItemCount(): Int = categories.size

        inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val categoryName: TextView = itemView.findViewById(R.id.categoryName)
            private val itemsContainer: ViewGroup = itemView.findViewById(R.id.itemsContainer)

            fun bind(category: GroceryCategory) {
                categoryName.text = category.name
                itemsContainer.removeAllViews()

                category.items.forEach { item ->
                    val checkBox = CheckBox(itemView.context).apply {
                        text = item
                        setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                selectedItems.add(item)
                            } else {
                                selectedItems.remove(item)
                            }
                        }
                    }
                    itemsContainer.addView(checkBox)
                }
            }
        }
    }
}