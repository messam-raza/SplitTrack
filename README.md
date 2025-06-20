# SplitTrack - Personal Expense Tracker & Group Expense Splitter

A modern Android application built with Kotlin that helps you track personal expenses and split group expenses efficiently. Features a beautiful gradient UI design with comprehensive expense management and smart settlement calculations.

## 📱 Features

### Personal Expense Tracking
- ✅ Add, edit, and delete personal expenses
- ✅ Categorize expenses (Food, Travel, Shopping, Entertainment, Utilities, Other)
- ✅ Interactive pie chart showing spending distribution by category
- ✅ Monthly expense summaries
- ✅ Date picker for expense dates
- ✅ Notes support for additional expense details

### Group Expense Splitting
- ✅ Create and manage expense groups
- ✅ Add multiple members to groups
- ✅ Track shared expenses with flexible splitting
- ✅ Smart settlement calculation algorithm
- ✅ Individual balance visualization
- ✅ Optimal settlement suggestions
- ✅ Color-coded balance indicators

### UI/UX
- ✅ Modern gradient design with Material Design 3
- ✅ Intuitive card-based layouts
- ✅ Smooth animations and transitions
- ✅ Responsive design for different screen sizes
- ✅ Clean typography and consistent spacing

### Technical Features
- ✅ SQLite database with Room ORM
- ✅ Kotlin coroutines for async operations
- ✅ MVVM architecture pattern
- ✅ Material Design 3 components
- ✅ MPAndroidChart for data visualization

## 🛠 Technology Stack

- *Language*: Kotlin
- *UI Framework*: Android Views with Material Design 3
- *Database*: Room (SQLite)
- *Charts*: MPAndroidChart
- *Architecture*: MVVM
- *Async*: Kotlin Coroutines
- *Build System*: Gradle with Kotlin DSL

## 📋 Requirements

- *Android Studio*: Arctic Fox or later
- *Minimum SDK*: 24 (Android 7.0)
- *Target SDK*: 35 (Android 15)
- *Compile SDK*: 35
- *Java Version*: 11

## 🚀 How to Run

### Prerequisites
1. Install [Android Studio](https://developer.android.com/studio)
2. Ensure you have Android SDK 35 installed
3. Set up an Android device or emulator (API level 24+)

### Installation Steps

1. *Clone the Repository*
   \\\`bash
   git clone https://github.com/yourusername/splittrack-android.git
   cd splittrack-android
   \\\`

2. *Open in Android Studio*
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it
   - Wait for Gradle sync to complete

3. *Configure Dependencies*
   - Android Studio should automatically download dependencies
   - If issues occur, try: Build → Clean Project → Rebuild Project

4. *Run the Application*
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon) or press Shift+F10
   - Select your target device
   - The app will install and launch automatically

### Alternative Installation Methods

#### Using APK (if available)
1. Download the APK from releases
2. Enable "Install from Unknown Sources" on your Android device
3. Install the APK file

#### Using Android Studio Build
1. In Android Studio: Build → Generate Signed Bundle/APK
2. Choose APK and follow the signing process
3. Install the generated APK on your device

## 📖 How to Use

### Personal Expenses
1. *Adding Expenses*:
   - Tap "Personal Expenses" on home screen
   - Tap the green "+" button
   - Fill in amount, select category, choose date, add notes
   - Tap "Save"

2. *Editing Expenses*:
   - Tap any expense in the list
   - Modify the details
   - Tap "Update"

3. *Deleting Expenses*:
   - Long press any expense
   - Confirm deletion in the dialog

4. *Viewing Analytics*:
   - Pie chart shows spending by category
   - Scroll through expense list for details

### Group Expenses
1. *Creating Groups*:
   - Tap "Group Splits" on home screen
   - Tap the green "+" button
   - Enter group name
   - Add at least 2 members
   - Tap "Create Group"

2. *Adding Group Expenses*:
   - Tap on any group
   - Tap the green "+" button
   - Enter expense details
   - Select who paid
   - Choose who the expense is for
   - Tap "Save"

3. *Viewing Balances*:
   - Individual balances show who owes what
   - Green = gets money back
   - Red = owes money
   - Gray = settled

4. *Settlement Suggestions*:
   - View optimal settlement recommendations
   - Minimizes number of transactions needed

## 🗂 Project Structure

\\\`
app/
├── src/main/
│   ├── java/com/messamraza/splittrack/
│   │   ├── database/
│   │   │   ├── AppDatabase.kt          # Room database
│   │   │   ├── Entities.kt             # Data entities
│   │   │   ├── Daos.kt                 # Database access objects
│   │   │   └── Converters.kt           # Type converters
│   │   ├── MainActivity.kt             # Home screen
│   │   ├── PersonalExpensesActivity.kt # Personal expense management
│   │   ├── AddExpenseActivity.kt       # Add/edit expense form
│   │   ├── GroupSplitsActivity.kt      # Group list screen
│   │   ├── GroupDetailActivity.kt      # Group detail with balances
│   │   ├── AddGroupActivity.kt         # Create group form
│   │   ├── AddGroupExpenseActivity.kt  # Add group expense form
│   │   └── *Adapter.kt                 # RecyclerView adapters
│   └── res/
│       ├── layout/                     # XML layouts
│       ├── drawable/                   # Icons and backgrounds
│       ├── values/                     # Colors, strings, themes
│       └── xml/                        # Data extraction rules
└── build.gradle.kts                   # App-level build configuration
\\\`

## 🐛 Known Issues

### Minor Issues
1. *Chart Animation*: Pie chart animations may lag on older devices (API < 26)
2. *Keyboard Overlap*: Soft keyboard may overlap input fields on small screens
3. *Date Validation*: No validation for future dates in expense entries
4. *Category Limit*: Fixed set of expense categories (no custom categories yet)

### Performance Notes
- Large expense lists (500+) may cause slight scrolling lag
- Group calculations are optimized but may take time with 20+ members
- Database operations are async but UI may briefly freeze on very old devices

## 🔮 Future Features

### Planned Enhancements
- [ ] *Firebase Integration*: Cloud sync and user authentication
- [ ] *PDF Export*: Generate expense reports and settlement summaries
- [ ] *Dark Mode*: Complete dark theme support
- [ ] *Custom Categories*: User-defined expense categories with icons
- [ ] *Recurring Expenses*: Support for monthly/weekly recurring expenses
- [ ] *Currency Support*: Multiple currency options
- [ ] *Expense Photos*: Attach receipt photos to expenses
- [ ] *Budget Tracking*: Set and monitor spending budgets
- [ ] *Notifications*: Reminders for settlements and budget limits
- [ ] *Data Backup*: Local and cloud backup options

### Advanced Features
- [ ] *Multi-language Support*: Localization for different languages
- [ ] *Expense Splitting Rules*: Custom splitting ratios (not just equal splits)
- [ ] *Group Templates*: Save and reuse group configurations
- [ ] *Analytics Dashboard*: Advanced spending insights and trends
- [ ] *Export Options*: CSV, Excel export functionality
- [ ] *Offline Sync*: Better offline support with sync when online
- [ ] *Widget Support*: Home screen widgets for quick expense entry
- [ ] *Voice Input*: Add expenses using voice commands

### Technical Improvements
- [ ] *Jetpack Compose*: Migrate UI to Compose for better performance
- [ ] *Dependency Injection*: Implement Hilt for better architecture
- [ ] *Unit Tests*: Comprehensive test coverage
- [ ] *CI/CD Pipeline*: Automated testing and deployment
- [ ] *Performance Optimization*: Database indexing and query optimization
- [ ] *Memory Management*: Better handling of large datasets

## 🤝 Contributing

I welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (\git checkout -b feature/amazing-feature\)
3. Commit your changes (\git commit -m 'Add amazing feature'\)
4. Push to the branch (\git push origin feature/amazing-feature\)
5. Open a Pull Request

### Development Guidelines
- Follow Kotlin coding conventions
- Add comments for complex logic
- Test on multiple device sizes
- Ensure backward compatibility (API 24+)
- Update README for new features

## 👨‍💻 Developer

*Messam Raza*
- Email: messamraza@example.com
- GitHub: [@messamraza](https://github.com/messamraza)

## 🙏 Acknowledgments

- *Material Design 3*: For the beautiful UI components
- *Room Database*: For robust local data storage
- *Android Jetpack*: For modern Android development tools

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Known Issues](#-known-issues) section
2. Search existing [GitHub Issues](https://github.com/yourusername/splittrack-android/issues)
3. Create a new issue with detailed description
4. Contact the developer directly

---

*Made with ❤ for better expense management*
\\\`

### Home Screen
Beautiful gradient background with two main options for personal expenses and group splits.

### Personal Expenses
- Interactive pie chart showing spending distribution
- Clean list of expenses with edit/delete functionality
- Easy expense entry with category selection

### Group Management
- Create groups with multiple members
- Track shared expenses with flexible splitting
- Individual balance visualization with color coding
- Smart settlement suggestions

### Modern UI
- Material Design 3 components
- Consistent gradient theme
- Smooth animations and transitions
- Responsive design for all screen sizes

---
