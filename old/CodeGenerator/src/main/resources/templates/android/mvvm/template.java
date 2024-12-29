/*package ${packageName};

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// Model
public class ${projectName}Model {
    private String data;

    public ${projectName}Model(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}

        // ViewModel
        public class ${projectName}ViewModel extends ViewModel {
    private MutableLiveData<String> data = new MutableLiveData<>();

    public LiveData<String> getData() {
        return data;
    }

    public void updateData(String newData) {
        data.setValue(newData);
    }
}

        // View
        public class ${projectName}Activity extends AppCompatActivity {
    private ${projectName}ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(${projectName}ViewModel.class);
        viewModel.getData().observe(this, data -> {
            // Update UI with data
        });
    }
}
*/


//file : resources/template/android/mvvm/template.java
package ${packageName};

import android.app.Application;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.*;
import androidx.room.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Model/Entity
@Entity(tableName = "items")
public class ${projectName}Model {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private long timestamp;
    private boolean isCompleted;

    public ${projectName}Model(String title, String description) {
        this.title = title;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
        this.isCompleted = false;
    }

    // Getters and Setters
    @NonNull
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}

        // Room DAO
        @Dao
        public interface ${projectName}Dao {
    @Query("SELECT * FROM items ORDER BY timestamp DESC")
            LiveData<List<${projectName}Model>> getAllItems();

    @Insert
    void insert(${projectName}Model item);

    @Update
    void update(${projectName}Model item);

    @Delete
    void delete(${projectName}Model item);

    @Query("SELECT * FROM items WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
            LiveData<List<${projectName}Model>> searchItems(String searchQuery);
}

        // Room Database
        @Database(entities = {${projectName}Model.class}, version = 1)
        public abstract class ${projectName}Database extends RoomDatabase {
    public abstract ${projectName}Dao ${projectName.toLowerCase()}Dao();

    private static volatile ${projectName}Database INSTANCE;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    static ${projectName}Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (${projectName}Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ${projectName}Database.class, "${projectName.toLowerCase()}_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

        // Repository
        public class ${projectName}Repository {
    private ${projectName}Dao ${projectName.toLowerCase()}Dao;
    private LiveData<List<${projectName}Model>> allItems;
    private ExecutorService executorService;

    public ${projectName}Repository(Application application) {
        ${projectName}Database db = ${projectName}Database.getDatabase(application);
        ${projectName.toLowerCase()}Dao = db.${projectName.toLowerCase()}Dao();
        allItems = ${projectName.toLowerCase()}Dao.getAllItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    LiveData<List<${projectName}Model>> getAllItems() {
        return allItems;
    }

    public void insert(${projectName}Model item) {
        executorService.execute(() -> ${projectName.toLowerCase()}Dao.insert(item));
    }

    public void update(${projectName}Model item) {
        executorService.execute(() -> ${projectName.toLowerCase()}Dao.update(item));
    }

    public void delete(${projectName}Model item) {
        executorService.execute(() -> ${projectName.toLowerCase()}Dao.delete(item));
    }

    public LiveData<List<${projectName}Model>> searchItems(String query) {
        return ${projectName.toLowerCase()}Dao.searchItems("%" + query + "%");
    }
}

        // ViewModel
        public class ${projectName}ViewModel extends AndroidViewModel {
    private ${projectName}Repository repository;
    private LiveData<List<${projectName}Model>> allItems;
    private MutableLiveData<${projectName}Model> selectedItem = new MutableLiveData<>();

    public ${projectName}ViewModel(Application application) {
        super(application);
        repository = new ${projectName}Repository(application);
        allItems = repository.getAllItems();
    }

    public LiveData<List<${projectName}Model>> getAllItems() { return allItems; }

    public void insert(${projectName}Model item) {
        repository.insert(item);
    }

    public void update(${projectName}Model item) {
        repository.update(item);
    }

    public void delete(${projectName}Model item) {
        repository.delete(item);
    }

    public void setSelectedItem(${projectName}Model item) {
        selectedItem.setValue(item);
    }

    public LiveData<${projectName}Model> getSelectedItem() {
        return selectedItem;
    }

    public LiveData<List<${projectName}Model>> searchItems(String query) {
        return repository.searchItems(query);
    }
}

        // Adapter
        public class ${projectName}Adapter extends ListAdapter<${projectName}Model, ${projectName}Adapter.ViewHolder> {
        private OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(${projectName}Model item);
            void onItemLongClick(${projectName}Model item);
            void onItemStatusChanged(${projectName}Model item, boolean isCompleted);
        }

        protected ${projectName}Adapter(OnItemClickListener listener) {
    super(DIFF_CALLBACK);
    this.listener = listener;
}

    private static final DiffUtil.ItemCallback<${projectName}Model> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<${projectName}Model>() {
        @Override
        public boolean areItemsTheSame(@NonNull ${projectName}Model oldItem,
        @NonNull ${projectName}Model newItem) {
        return oldItem.getId() == newItem.getId();
                }

        @Override
        public boolean areContentsTheSame(@NonNull ${projectName}Model oldItem,
        @NonNull ${projectName}Model newItem) {
        return oldItem.getTitle().equals(newItem.getTitle()) &&
        oldItem.getDescription().equals(newItem.getDescription()) &&
        oldItem.isCompleted() == newItem.isCompleted();
                }
                        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_${projectName.toLowerCase()}, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ${projectName}Model item = getItem(position);
            holder.bind(item, listener);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleText;
            private final TextView descriptionText;
            private final CheckBox completedCheck;
            private final TextView timestampText;
            private final MaterialCardView cardView;

            ViewHolder(View itemView) {
                super(itemView);
                titleText = itemView.findViewById(R.id.text_title);
                descriptionText = itemView.findViewById(R.id.text_description);
                completedCheck = itemView.findViewById(R.id.check_completed);
                timestampText = itemView.findViewById(R.id.text_timestamp);
                cardView = itemView.findViewById(R.id.card_view);
            }

            void bind(final ${projectName}Model item, final OnItemClickListener listener) {
                titleText.setText(item.getTitle());
                descriptionText.setText(item.getDescription());
                completedCheck.setChecked(item.isCompleted());

                // Format timestamp
                String formattedDate = new SimpleDateFormat("MMM dd, yyyy HH:mm",
                        Locale.getDefault()).format(new Date(item.getTimestamp()));
                timestampText.setText(formattedDate);

                // Set strike-through text if completed
                if (item.isCompleted()) {
                    titleText.setPaintFlags(titleText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    titleText.setPaintFlags(titleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                // Setup click listeners
                cardView.setOnClickListener(v -> listener.onItemClick(item));
                cardView.setOnLongClickListener(v -> {
                    listener.onItemLongClick(item);
                    return true;
                });

                completedCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (buttonView.isPressed()) { // Only trigger if user changed it
                        listener.onItemStatusChanged(item, isChecked);
                    }
                });
            }
        }
}

        // Activity
        public class ${projectName}Activity extends AppCompatActivity {
    private ${projectName}ViewModel viewModel;
    private ${projectName}Adapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_${projectName.toLowerCase()});

        viewModel = new ViewModelProvider(this).get(${projectName}ViewModel.class);
        setupViews();
        observeViewModel();
    }

    private void setupViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_items);
        adapter = new ${projectName}Adapter(new OnItemClickListener() {
            @Override
            public void onItemClick(${projectName}Model item) {
                showEditDialog(item);
            }

            @Override
            public void onItemLongClick(${projectName}Model item) {
                showDeleteDialog(item);
            }

            @Override
            public void onItemStatusChanged(${projectName}Model item, boolean isCompleted) {
                item.setCompleted(isCompleted);
                viewModel.update(item);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());

        setupSearchView();
    }

    private void observeViewModel() {
        viewModel.getAllItems().observe(this, items -> {
            adapter.submitList(items);
        });

        viewModel.getSelectedItem().observe(this, item -> {
            if (item != null) {
                showEditDialog(item);
            }
        });
    }
    private void setupSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchItems(newText);
                return true;
            }
        });
    }

    private void searchItems(String query) {
        viewModel.searchItems(query).observe(this, items -> {
            adapter.submitList(items);
        });
    }

    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        TextInputEditText titleInput = dialogView.findViewById(R.id.edit_title);
        TextInputEditText descriptionInput = dialogView.findViewById(R.id.edit_description);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.add_item)
                .setView(dialogView)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String description = descriptionInput.getText().toString().trim();

                    if (validateInput(title)) {
                        ${projectName}Model newItem = new ${projectName}Model(title, description);
                        viewModel.insert(newItem);
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Get dialog button and disable it initially if title is empty
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);

        // Enable/disable add button based on title input
        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveButton.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showEditDialog(${projectName}Model item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
        TextInputEditText titleInput = dialogView.findViewById(R.id.edit_title);
        TextInputEditText descriptionInput = dialogView.findViewById(R.id.edit_description);
        CheckBox completedCheck = dialogView.findViewById(R.id.check_completed);

        titleInput.setText(item.getTitle());
        descriptionInput.setText(item.getDescription());
        completedCheck.setChecked(item.isCompleted());

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.edit_item)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String description = descriptionInput.getText().toString().trim();

                    if (validateInput(title)) {
                        item.setTitle(title);
                        item.setDescription(description);
                        item.setCompleted(completedCheck.isChecked());
                        viewModel.update(item);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setNeutralButton(R.string.delete, (dialog, which) -> {
                    showDeleteDialog(item);
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Get dialog button and disable it initially if title is empty
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(!titleInput.getText().toString().trim().isEmpty());

        // Enable/disable save button based on title input
        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveButton.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showDeleteDialog(${projectName}Model item) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_item)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    viewModel.delete(item);
                    Snackbar.make(findViewById(android.R.id.content),
                                    R.string.item_deleted, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, v -> viewModel.insert(item))
                            .show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private boolean validateInput(String title) {
        if (title.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content),
                    R.string.error_empty_title, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Dialog and UI methods similar to MVC version but using ViewModel
}