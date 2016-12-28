package com.soywiz.korio.vfs

import com.soywiz.korio.async.sync
import com.soywiz.korio.async.toList
import org.junit.Assert
import org.junit.Test

class ZipVfsTest {
	@Test
	fun testZipUncompressed() = sync {
		val helloZip = ResourcesVfs()["hello.zip"].openAsZip()

	Assert.assertEquals(
		"[VfsStat(file=ZipVfs(ResourcesVfs[hello.zip])[hello], exists=true, isDirectory=true, size=0, device=-1, inode=0, mode=511, owner=nobody, group=nobody, createTime=1482773092000, modifiedTime=1482773092000, lastAccessTime=1482773092000, extraInfo=null)]",
		helloZip.list().toList().map { it.stat() }.toString()
	)

	Assert.assertEquals(
		"[VfsStat(file=ZipVfs(ResourcesVfs[hello.zip])[hello/world.txt], exists=true, isDirectory=false, size=12, device=-1, inode=1, mode=511, owner=nobody, group=nobody, createTime=1482773092000, modifiedTime=1482773092000, lastAccessTime=1482773092000, extraInfo=null)]",
		helloZip["hello"].list().toList().map { it.stat() }.toString()
	)

	Assert.assertEquals(
		"VfsStat(file=ZipVfs(ResourcesVfs[hello.zip])[hello/world.txt], exists=true, isDirectory=false, size=12, device=-1, inode=1, mode=511, owner=nobody, group=nobody, createTime=1482773092000, modifiedTime=1482773092000, lastAccessTime=1482773092000, extraInfo=null)",
		helloZip["hello/world.txt"].stat().toString()
	)
	Assert.assertEquals(
		"HELLO WORLD!",
		helloZip["hello/world.txt"].readString()
	)
		Assert.assertEquals(
			"Mon Dec 26 18:24:52 CET 2016",
			helloZip["hello/world.txt"].stat().createDate.toString()
		)
	}

	@Test
	fun testZipCompressed() = sync {
		val helloZip = ResourcesVfs()["compressedHello.zip"].openAsZip()
		Assert.assertEquals(
			"HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO WORLD!",
			helloZip["hello/compressedWorld.txt"].readString()
		)
		Assert.assertEquals(
			"[hello, hello/compressedWorld.txt, hello/world.txt]",
			helloZip.listRecursive().toList().map { it.fullname }.toString()
		)
	}
}
