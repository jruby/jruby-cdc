require 'java'
require 'fileutils'
require 'rbconfig'

module JRuby
  ZipFile = java.util.zip.ZipFile
  ZipEntry = java.util.zip.ZipEntry

  # extract META-INF/jruby.home/**/* to somewhere.
  class Extract
    def initialize(dir = nil)
      @this_archive = __FILE__ =~ /file:([^!]*)!.*/ && $1
      raise "error: can't locate enclosed archive" if @this_archive.nil?
      @zip = ZipFile.new(@this_archive)
      @destination = dir || Config::CONFIG['prefix']
    end

    def entries
      enum = @zip.entries
      def enum.each
        while hasMoreElements
          yield nextElement
        end
      end
      enum
    end

    def extract
      entries.each do |entry|
        if entry.name =~ %r"^META-INF/jruby.home/"
          path = write_entry entry
          FileUtils.chmod 0755, path if entry.name =~ %r"jruby.home/bin/"
        end
      end
      puts "copying #{@this_archive} to #{@destination}/lib"
      FileUtils.cp(@this_archive, "#{@destination}/lib")
    end
    
    def write_entry(entry)
      entry_path = "#{@destination.sub(%r{/$},'')}/#{entry.name.sub(%r{META-INF/jruby.home/},'')}"
      puts "creating #{entry_path}"
      FileUtils.mkdir_p(File.dirname(entry_path))
      instream = @zip.getInputStream(entry)
      outstream = java.io.FileOutputStream.new(entry_path)
      buffer = java.lang.reflect.Array.newInstance(java.lang.Byte::TYPE, 8192)
      while (num_read = instream.read(buffer)) != -1
        outstream.write buffer, 0, num_read
      end
      entry_path
    ensure
      instream.close if instream
      outstream.close if outstream
    end
  end
end

def to
  JRuby::Extract.new(ARGV.first).extract
end

def to_home
  JRuby::Extract.new.extract
end
