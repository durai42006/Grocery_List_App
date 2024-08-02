import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.grozon.R
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

class CellAdapter(private val sheet: Sheet, private var numColumns: Int) : RecyclerView.Adapter<CellAdapter.CellViewHolder>() {

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cellEditText: EditText = itemView.findViewById(R.id.cellEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)
        return CellViewHolder(view)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val rowIndex = position / numColumns
        val colIndex = position % numColumns

        val row = sheet.getRow(rowIndex) ?: sheet.createRow(rowIndex)
        val cell = row.getCell(colIndex) ?: row.createCell(colIndex)

        holder.cellEditText.setText(cell.toString())

        holder.cellEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newValue = holder.cellEditText.text.toString()
                cell.setCellValue(newValue)
            }
        }
        val border = GradientDrawable()
        border.setStroke(2, holder.itemView.context.getColor(android.R.color.black))
        holder.cellEditText.background = border
    }

    override fun getItemCount(): Int {
        return sheet.physicalNumberOfRows * numColumns
    }

    fun addRow() {
        val rowIndex = sheet.physicalNumberOfRows
        sheet.createRow(rowIndex)
        notifyItemRangeInserted(rowIndex * numColumns, numColumns)
    }

    fun addColumn() {
        val numRows = sheet.physicalNumberOfRows
        for (i in 0 until numRows) {
            val row = sheet.getRow(i) ?: sheet.createRow(i)
            row.createCell(numColumns) // Add cell at the new column index
        }
        numColumns++ // Increment the number of columns
        notifyDataSetChanged() // Notify adapter about the data change
    }
}
