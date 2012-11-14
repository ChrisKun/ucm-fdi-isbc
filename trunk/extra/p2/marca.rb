require 'nokogiri'
require 'open-uri'
require 'C:\Documents and Settings\Xavi\Mis documentos\Programacion\Ruby\futbol.rb'

def infoJornada(anyo, jornada)

  anyo2 = anyo.to_s[-2,2].to_i+1
  anyo2 = anyo2.to_s

  if (anyo2.length != 2) then
    anyo2[0], anyo2[1] = '0', anyo2[0]
  end

  puts "Procesando la siguiente pagina: "
  puts "http://www.marca.com/estadisticas/futbol/primera/#{anyo}_#{anyo2}/jornada_#{jornada}/"
  

  doc = Nokogiri::HTML(open("http://www.marca.com/estadisticas/futbol/primera/#{anyo}_#{anyo2}/jornada_#{jornada}/"))
  doc2 = doc.xpath("//td")
  doc3 = []
  doc2.each { |line| doc3.push(line.text) }
  
  tmp = doc3[0..39]
  partidos = []
  tmp.each_slice(4) do |slice|
    res = slice[1].split('-')
    partidos.push(Partido.new(slice[0],res[0],slice[2],res[1]))
  end
  
  tmp = doc3[40..300]
  clasificacion = []
  tmp.each_slice(9) do |s|
    clasificacion.push(Clasificacion.new(s[0],s[1],s[2],s[3],s[4],s[5],s[6],s[7],s[8]))
  end
  
  jornada = Jornada.new(partidos, clasificacion)
  return jornada.to_file
  rescue Exception
  puts "! - No se ha podido cargar la jornada #{jornada} del anyo #{anyo}"
end

puts File.directory?(".")
for i in (2000..2012) do
  f = File.new("C:/Documents and Settings/Xavi/Mis documentos/Programacion/Ruby/infoMarca#{i}.txt", "w")  
  for j in (1..38) do
    f.puts(infoJornada(i,j))
  end
  f.close
end
