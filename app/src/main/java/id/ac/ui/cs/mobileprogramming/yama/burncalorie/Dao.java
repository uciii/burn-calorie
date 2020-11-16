package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@androidx.room.Dao
public interface Dao {
    @Insert(onConflict = REPLACE)
    void insert(SummaryData summaryData);

    @Delete
    void delete(SummaryData summaryData);

    @Query("SELECT * FROM burn_summary")
    List<SummaryData> getAll();
}
