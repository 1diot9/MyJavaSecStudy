# LargeDatasetExcelExportHandler XML 参数格式分析

## 概述

`LargeDatasetExcelExportHandler#getEntity` 方法从 HTTP 请求的 `params` 参数读取 XML 字符串，并使用 `XMLableReader` 将其反序列化为 `LargeDatasetExcelExportJavaScript` 对象。

**关键代码位置**: `com/fr/nx/app/web/v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java:139-144`

```java
private LargeDatasetExcelExportJavaScript getEntity(HttpServletRequest httpServletRequest) throws XMLStreamException {
    String hTTPRequestParameter = WebUtils.getHTTPRequestParameter(httpServletRequest, "params");
    LargeDatasetExcelExportJavaScript largeDatasetExcelExportJavaScript = new LargeDatasetExcelExportJavaScript();
    XMLableReader.createXMLableReader(hTTPRequestParameter).readXMLObject(largeDatasetExcelExportJavaScript);
    return largeDatasetExcelExportJavaScript;
}
```

## XML 解析链分析

### 1. LargeDatasetExcelExportJavaScript.readXML()

**位置**: `com/fr/nx/app/web/handler/export/largeds/bean/LargeDatasetExcelExportJavaScript.java:42-55`

```java
@Override
public void readXML(XMLableReader xMLableReader) {
    super.readXML(xMLableReader);  // 调用父类解析 Parameters
    if (xMLableReader.isChildNode() && "LargeDatasetExcelExportJS".equals(xMLableReader.getTagName())) {
        setFileName(xMLableReader.getAttrAsString("exportFileName", ""));
        setDsName(xMLableReader.getAttrAsString("dsName", ""));
        LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap) ((JSONObject)
            JSONFactory.createJSON(JSON.OBJECT, xMLableReader.getAttrAsString("colNames", ""))).getMap();
        // ... 处理 colNamesMap
        setColNamesMap(linkedHashMap);
    }
}
```

**解析属性**:
| 属性名 | 类型 | 说明 |
|--------|------|------|
| `exportFileName` | String | 导出文件名 |
| `dsName` | String | 数据集名称（必填） |
| `colNames` | JSON String | 列名映射，格式为 JSON 对象字符串 |

### 2. AbstractJavaScript.readXML() (父类)

**位置**: `com/fr/js/AbstractJavaScript.java:144-148`

```java
@Override
public void readXML(XMLableReader xMLableReader) {
    if (xMLableReader.isChildNode() && ParameterProvider.ARRAY_XML_TAG.equals(xMLableReader.getTagName())) {
        setParameters(BaseXMLUtils.readParameters(xMLableReader));
    }
}
```

**关键常量**: `ParameterProvider.ARRAY_XML_TAG = "Parameters"`

### 3. BaseXMLUtils.readParameters()

**位置**: `com/fr/base/BaseXMLUtils.java:297-322`

```java
public static Parameter[] readParameters(XMLableReader xMLableReader) {
    final ArrayList arrayList = new ArrayList();
    xMLableReader.readXMLObject(new XMLReadable() {
        @Override
        public void readXML(XMLableReader xMLableReader2) {
            if (xMLableReader2.isChildNode()) {
                if (xMLableReader2.getTagName().equals(ParameterProvider.XML_TAG)) {
                    arrayList.add(BaseXMLUtils.readParameter(xMLableReader2));
                }
                // ... 其他参数类型处理
            }
        }
    });
    return (Parameter[]) arrayList.toArray(new Parameter[arrayList.size()]);
}
```

**关键常量**: `ParameterProvider.XML_TAG = "Parameter"`

### 4. Parameter.readXML()

**位置**: `com/fr/base/Parameter.java:92-102`

```java
public void readXML(XMLableReader xMLableReader) {
    if (xMLableReader.isChildNode()) {
        String tagName = xMLableReader.getTagName();
        if (XMLConstants.OLD_OBJECT_TAG.equals(tagName) || XMLConstants.OBJECT_TAG.equals(tagName)) {
            setValue(GeneralXMLTools.readObject(xMLableReader));
        } else if ("Attributes".equals(tagName) && xMLableReader.getAttrAsString("name", null) != null) {
            setName(xMLableReader.getAttrAsString("name", null));
        }
    }
}
```

**关键常量**:
- `XMLConstants.OBJECT_TAG = "O"`
- `XMLConstants.OLD_OBJECT_TAG = "Object"`

### 5. GeneralXMLTools.readObject()

**位置**: `com/fr/general/xml/GeneralXMLTools.java:293-304`

读取对象值，根据 `t` 属性判断类型：
- `String` / `S` - 字符串
- `Double` / `D` - 双精度浮点数
- `Boolean` / `B` - 布尔值
- `Integer` / `I` - 整数
- `Long` / `L` - 长整数
- `Date` - 日期
- `Formula` / `RFormula` - 公式
- 等等...

## 预期 XML 格式

### 关键说明

**外层必须包含 `<R>` 包装标签**，原因如下：
- `LargeDatasetExcelExportJavaScript.readXML()` 中使用 `isChildNode()` 判断（第44行）
- 只有当 `LargeDatasetExcelExportJS` 是**子节点**时，`isChildNode()` 才返回 true，属性才会被解析
- 如果 `LargeDatasetExcelExportJS` 是根节点，属性将被忽略，导致 `fileName` 和 `dsName` 为 null

### 完整 XML 结构

```xml
<R class="com.fr.nx.app.web.handler.export.largeds.bean.LargeDatasetExcelExportJavaScript">
    <LargeDatasetExcelExportJS
        exportFileName="导出文件名"
        dsName="数据集名称"
        colNames='{"列1":"值1","列2":"值2"}'>
        <Parameters>
            <Parameter>
                <Attributes name="参数名1"/>
                <O t="String">参数值1</O>
            </Parameter>
            <Parameter>
                <Attributes name="参数名2"/>
                <O t="Double">123.45</O>
            </Parameter>
            <Parameter>
                <Attributes name="参数名3"/>
                <O t="Formula">=SUM(A1:A10)</O>
            </Parameter>
        </Parameters>
    </LargeDatasetExcelExportJS>
</R>
```

### 最简 XML 结构（无参数）

```xml
<R>
    <LargeDatasetExcelExportJS
        exportFileName="测试导出"
        dsName="ds1"
        colNames='{}'/>
</R>
```

### 带参数的 XML 结构

```xml
<R>
    <LargeDatasetExcelExportJS
        exportFileName="报表导出"
        dsName="product_sales"
        colNames='{"product":"产品","amount":"金额","date":"日期"}'>
        <Parameters>
            <Parameter>
                <Attributes name="startDate"/>
                <O t="String">2024-01-01</O>
            </Parameter>
            <Parameter>
                <Attributes name="endDate"/>
                <O t="String">2024-12-31</O>
            </Parameter>
        </Parameters>
    </LargeDatasetExcelExportJS>
</R>
```

### Formula 类型的特殊说明

**重要**：Formula 类型的 XML 格式需要特别注意，`class` 属性是必须的！

#### Formula 写入格式（参考）

当系统写入 Formula 时，生成的 XML 格式如下：
```xml
<O t="Formula" class="com.fr.base.Formula">
    <Attributes>=SUM(A1:A10)</Attributes>
</O>
```

#### Formula 读取格式要求

**必须包含 `class` 属性**，否则 `getAttrAsClass()` 可能无法正确解析：

```xml
<!-- 正确：有 class 属性 -->
<O t="Formula" class="com.fr.base.Formula">
    <Attributes>sql('FRDemo', '...')</Attributes>
</O>

<!-- 可能有问题：缺少 class 属性 -->
<O t="Formula">
    <Attributes>sql('FRDemo', '...')</Attributes>
</O>
```

#### 完整的 Parameter with Formula 示例

```xml
<R>
    <LargeDatasetExcelExportJS
        exportFileName="报表导出"
        dsName="ds1"
        colNames="{}">
        <Parameters>
            <Parameter>
                <Attributes name="sqlParam"/>
                <O t="Formula" class="com.fr.base.Formula">
                    <Attributes>sql('FRDemo', 'SELECT * FROM table')</Attributes>
                </O>
            </Parameter>
        </Parameters>
    </LargeDatasetExcelExportJS>
</R>
```

#### 为什么 content 可能为空？

如果 Formula 的 content 为空字符串，可能原因：

1. **缺少 `class` 属性**：`getAttrAsClass()` 在 `getFormula()` 中可能无法正确解析类名
2. **`isChildNode()` 返回 false**：`readXMLObject()` 没有正确推进到子节点
3. **`getTagName()` 不匹配**：当前节点不是 `<Attributes>` 标签
4. **`getElementValue()` 返回 null**：`<Attributes>` 标签为空或格式错误

**调试建议**：在 XML 中添加 `class="com.fr.base.Formula"` 属性。

### 常见错误格式（会导致解析失败）

**错误 1**：缺少外层 `<R>` 标签
```xml
<!-- 错误：LargeDatasetExcelExportJS 作为根节点 -->
<LargeDatasetExcelExportJS exportFileName="xxx" dsName="ds1" colNames="{}">
    <Parameters>...</Parameters>
</LargeDatasetExcelExportJS>
```

**错误 2**：Formula 缺少 `class` 属性
```xml
<!-- 错误：可能导致 Formula content 为空 -->
<O t="Formula">
    <Attributes>sql(...)</Attributes>
</O>
```

**错误 3**：使用 `<Object>` 标签（虽然兼容但不推荐）
```xml
<!-- 不推荐：应使用 <O> 标签 -->
<Object t="Formula">...</Object>
```

**正确**：使用 `<O>` 标签
```xml
<O t="Formula">...</O>
```

## 字段类型映射

| XML `t` 属性值 | Java 类型 | 示例值 |
|---------------|----------|--------|
| `String` / `S` | String | `<O t="String">文本值</O>` |
| `Double` / `D` | Double | `<O t="Double">123.45</O>` |
| `Boolean` / `B` | Boolean | `<O t="Boolean">true</O>` |
| `Integer` / `I` | Integer | `<O t="Integer">100</O>` |
| `Long` / `L` | Long | `<O t="Long">9999999999</O>` |
| `Date` | Date | `<O t="Date">2024-01-01</O>` |
| `Formula` | Formula | `<O t="Formula">=SUM(A1)</O>` |
| `NULL` | Primitive.NULL | `<O t="NULL"/>` |

## 数据流程图

### 生成 XML 流程（writeXMLableAsString）

```
LargeDatasetExcelExportJavaScript 对象
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│  GeneralXMLTools.writeXMLableAsString(this)            │
│  ├── 写入 <R class="..."> 外层标签                     │
│  ├── 调用 writeXML()                                    │
│  │   └── <LargeDatasetExcelExportJS 属性...>           │
│  │   └── <Parameters>...</Parameters>                   │
│  └── 写入 </R> 结束标签                                 │
└─────────────────────────────────────────────────────────┘
        │
        ▼
生成的 XML 字符串（用于 HTTP 请求 params 参数）
```

### 解析 XML 流程（getEntity）

```
HTTP Request (params 参数)
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│  XMLableReader.createXMLableReader(params)             │
│      .readXMLObject(LargeDatasetExcelExportJavaScript) │
└─────────────────────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│  XML 解析器遍历                                         │
│  ├── 根节点 <R>（初始化时设置）                         │
│  ├── 子节点 <LargeDatasetExcelExportJS>                 │
│  │   └── isChildNode() = true ✓                        │
│  │   └── 读取属性：exportFileName, dsName, colNames     │
│  │   └── super.readXML() → 解析 <Parameters>           │
│  │       └── BaseXMLUtils.readParameters()             │
│  │           └── 读取每个 <Parameter> 子标签            │
│  │               ├── <Attributes name="xxx"/>           │
│  │               └── <O t="类型">值</O>                │
│  └── 其他子节点...                                      │
└─────────────────────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│  LargeDatasetExcelExportJavaScript 对象                 │
│  ├── fileName: String（有值）                          │
│  ├── dsName: String（有值）                            │
│  ├── colNamesMap: LinkedHashMap<String, Object>         │
│  └── parameters: ParameterProvider[]                    │
└─────────────────────────────────────────────────────────┘
```

### 为什么需要 <R> 外层标签？

```java
// LargeDatasetExcelExportJavaScript.readXML() 第44行
if (xMLableReader.isChildNode() && "LargeDatasetExcelExportJS".equals(xMLableReader.getTagName())) {
    // 只有 isChildNode() 返回 true 时才会读取属性
    setFileName(xMLableReader.getAttrAsString("exportFileName", ""));
    setDsName(xMLableReader.getAttrAsString("dsName", ""));
    // ...
}
```

- **正确**：`<R><LargeDatasetExcelExportJS .../></R>` → `LargeDatasetExcelExportJS` 是子节点 → `isChildNode() = true` → 属性被读取
- **错误**：`<LargeDatasetExcelExportJS .../>` → `LargeDatasetExcelExportJS` 是根节点 → `isChildNode() = false` → 属性被忽略 → `fileName` 和 `dsName` 为 null

## 安全关注点

1. **XML 格式要求**: 必须包含外层 `<R>` 标签，否则属性无法解析，导致 `dsName` 为 null 抛出异常。

2. **XML 外部实体 (XXE)**: `XMLableReader` 在第 49-50 行禁用了外部实体解析：
   ```java
   if (factory.isPropertySupported(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)) {
       factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
   }
   ```

3. **参数可控性**: `params` 参数来自 HTTP 请求，攻击者可以完全控制 XML 内容，包括：
   - `dsName` - 数据集名称
   - `colNames` - 列名映射
   - `Parameters` - 参数列表及其值（支持 Formula 公式执行）

4. **公式注入风险**: `<O t="Formula">` 中的内容会被作为公式执行，存在代码执行风险。

## 相关代码文件

| 文件 | 路径 |
|------|------|
| LargeDatasetExcelExportHandler | `com/fr/nx/app/web/v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java` |
| LargeDatasetExcelExportJavaScript | `com/fr/nx/app/web/handler/export/largeds/bean/LargeDatasetExcelExportJavaScript.java` |
| AbstractJavaScript | `com/fr/js/AbstractJavaScript.java` |
| Parameter | `com/fr/base/Parameter.java` |
| BaseXMLUtils | `com/fr/base/BaseXMLUtils.java` |
| XMLableReader | `com/fr/stable/xml/XMLableReader.java` |
| GeneralXMLTools | `com/fr/general/xml/GeneralXMLTools.java` |
| BaseObjectTokenizer | `com/fr/base/BaseObjectTokenizer.java` |