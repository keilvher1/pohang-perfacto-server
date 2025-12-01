#!/bin/bash

# Markdown 문서를 PDF로 변환하는 스크립트
# 사용법: ./convert_to_pdf.sh

echo "📄 Perfacto 가이드 문서를 PDF로 변환합니다..."

# 현재 디렉토리
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# HTML 템플릿이 포함된 CSS
cat > style.css << 'EOF'
body {
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Noto Sans', Helvetica, Arial, sans-serif;
    line-height: 1.6;
    color: #24292e;
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
    background: white;
}

h1 {
    color: #0366d6;
    border-bottom: 3px solid #0366d6;
    padding-bottom: 10px;
    margin-top: 30px;
}

h2 {
    color: #24292e;
    border-bottom: 1px solid #eaecef;
    padding-bottom: 8px;
    margin-top: 24px;
}

h3 {
    color: #24292e;
    margin-top: 20px;
}

code {
    background: #f6f8fa;
    padding: 2px 6px;
    border-radius: 3px;
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
    font-size: 85%;
}

pre {
    background: #f6f8fa;
    padding: 16px;
    border-radius: 6px;
    overflow-x: auto;
    line-height: 1.45;
}

pre code {
    background: transparent;
    padding: 0;
    font-size: 12px;
}

blockquote {
    border-left: 4px solid #dfe2e5;
    padding-left: 16px;
    color: #6a737d;
    margin: 0;
}

table {
    border-collapse: collapse;
    width: 100%;
    margin: 16px 0;
}

table th,
table td {
    border: 1px solid #dfe2e5;
    padding: 8px 13px;
}

table th {
    background: #f6f8fa;
    font-weight: 600;
}

table tr:nth-child(even) {
    background: #f6f8fa;
}

a {
    color: #0366d6;
    text-decoration: none;
}

a:hover {
    text-decoration: underline;
}

.page-break {
    page-break-after: always;
}

@media print {
    body {
        max-width: 100%;
        padding: 0;
    }

    h1 {
        page-break-before: always;
    }

    h1:first-of-type {
        page-break-before: avoid;
    }

    pre, blockquote {
        page-break-inside: avoid;
    }
}
EOF

echo "✅ 스타일 시트 생성 완료"

# 각 문서를 HTML로 변환
convert_md_to_html() {
    local md_file=$1
    local html_file="${md_file%.md}.html"

    if [ -f "$md_file" ]; then
        echo "🔄 변환 중: $md_file -> $html_file"

        pandoc "$md_file" \
            -f markdown \
            -t html \
            --standalone \
            --css=style.css \
            --metadata title="Perfacto Guide" \
            -o "$html_file"

        echo "✅ 완료: $html_file"
        return 0
    else
        echo "❌ 파일을 찾을 수 없습니다: $md_file"
        return 1
    fi
}

# 모든 가이드 문서 변환
echo ""
echo "📚 문서 변환 시작..."
echo ""

convert_md_to_html "PERFACTO_API_GUIDE.md"
convert_md_to_html "SETUP_GUIDE.md"
convert_md_to_html "AWS_SETUP_STEP_BY_STEP.md"
convert_md_to_html "PERFACTO_BACKEND_SUMMARY.md"
convert_md_to_html "PERFACTO_WBS.md"

echo ""
echo "✅ 모든 HTML 변환 완료!"
echo ""
echo "📖 PDF로 출력하는 방법:"
echo ""
echo "1️⃣  Safari 사용 (권장):"
echo "   - HTML 파일을 Safari로 열기"
echo "   - 파일 → PDF로 내보내기 (⌘P)"
echo "   - PDF로 저장 선택"
echo ""
echo "2️⃣  Chrome 사용:"
echo "   - HTML 파일을 Chrome으로 열기"
echo "   - 인쇄 (⌘P)"
echo "   - 대상: PDF로 저장"
echo ""
echo "3️⃣  자동으로 열기:"
echo ""

# 자동으로 HTML 파일 열기
for html in *.html; do
    if [ -f "$html" ]; then
        echo "   open \"$html\""
    fi
done

echo ""
echo "🎉 변환 완료! 위 명령어로 파일을 열고 PDF로 저장하세요."
echo ""
echo "💡 Tip: Chrome에서 인쇄 설정"
echo "   - 여백: 없음"
echo "   - 배경 그래픽: 체크"
echo "   - 용지 크기: A4"
EOF

chmod +x convert_to_pdf.sh
