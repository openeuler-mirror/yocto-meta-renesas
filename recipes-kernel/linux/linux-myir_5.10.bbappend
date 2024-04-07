#FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

#SRC_URI_append = " \
#	file://0001-Fixed-an-issue-that-caused-flicker-when-outputting-t.patch \
#"

PROVIDES += "${KERNEL_PACKAGE_NAME}-img"

PACKAGES += "${KERNEL_PACKAGE_NAME}-img"
FILES:${KERNEL_PACKAGE_NAME}-img = "/boot/Image"
