Сборка:
1. Открой https://fabricmc.net/develop , выбери 1.21.11 и подставь актуальные yarn_mappings / loader_version / fabric_version в gradle.properties.
2. Скопируй папку gradle wrapper из любого Fabric-шаблона (или выполни `gradle wrapper`).
3. ./gradlew build  ->  build/libs/legitclient-1.0.0.jar в папку mods (нужен Fabric API).

Управление:
- Right Shift: открыть/закрыть Click GUI
- Кнопка модуля: вкл/выкл
- Кнопка Bind: нажми её, затем нужную клавишу (Backspace - снять бинд, Esc - отмена)
- Бинды работают в игре, когда экран не открыт
